package com.BTL_JAVA.BTL.Service;

import com.BTL_JAVA.BTL.DTO.Request.Auth.AuthenticationRequest;
import com.BTL_JAVA.BTL.DTO.Request.Auth.IntrospectRequest;
import com.BTL_JAVA.BTL.DTO.Request.Auth.LogoutRequest;
import com.BTL_JAVA.BTL.DTO.Request.Auth.RefreshRequest;
import com.BTL_JAVA.BTL.DTO.Response.Auth.AuthenticationResponse;
import com.BTL_JAVA.BTL.DTO.Response.Auth.IntrospectResponse;
import com.BTL_JAVA.BTL.Entity.InvalidtedToken;
import com.BTL_JAVA.BTL.Entity.User;
import com.BTL_JAVA.BTL.Exception.AppException;
import com.BTL_JAVA.BTL.Exception.ErrorCode;
import com.BTL_JAVA.BTL.Repository.InvalidtedTokenRepository;
import com.BTL_JAVA.BTL.Repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimNames;
import com.nimbusds.jwt.JWTClaimsSet;

import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;

    InvalidtedTokenRepository invalidtedTokenRepository;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected  String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected  long VALIDATION_DURATION;

    @NonFinal
    @Value("${jwt.refresh-duration}")
    protected  long REFRESH_DURATION;

    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token=request.getToken();
        boolean isValid=true;
        SignedJWT signedJWT=null;

       try {
           signedJWT= verifyToken(token,false);
       }catch (AppException | JOSEException|ParseException e){
        isValid=false;
       }
        return IntrospectResponse.builder()
                .valid(isValid)
                .userId(Objects.nonNull(signedJWT)? signedJWT.getJWTClaimsSet().getSubject():null)
                .build();
    }


   public AuthenticationResponse authenticated(AuthenticationRequest request) {

        log.info("SIGNKEY: {}",SIGNER_KEY);
          var user = userRepository.findByFullName(request.getFullName())
                  .orElseThrow(() ->new AppException(ErrorCode.USER_NOT_EXISTED));

        PasswordEncoder  passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticate= passwordEncoder.matches(request.getPassword(), user.getPassword());
        if(!authenticate){
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
        var token= generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();

    }

    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        try {
            var signToken = verifyToken(request.getToken(), true);

            String jit=signToken.getJWTClaimsSet().getJWTID();
            Date expiryTime=signToken.getJWTClaimsSet().getExpirationTime();

            InvalidtedToken invalidtedToken=InvalidtedToken.builder()
                    .id(jit)
                    .expiryTime(expiryTime)
                    .build();
            invalidtedTokenRepository.save(invalidtedToken);
        }catch (AppException e){
            log.info("Token already expired");
        }

    }

    public AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException {

        var signToken = verifyToken(request.getToken(),true);

        String jit=signToken.getJWTClaimsSet().getJWTID();
        Date expiryTime=signToken.getJWTClaimsSet().getExpirationTime();

        InvalidtedToken invalidtedToken=InvalidtedToken.builder()
                .id(jit)
                .expiryTime(expiryTime)
                .build();
        invalidtedTokenRepository.save(invalidtedToken);


        var username=signToken.getJWTClaimsSet().getSubject();

        var user=userRepository.findByFullName(username).orElseThrow(() ->new AppException(ErrorCode.UNAUTHENTICATED));

        var token= generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();

    }

    private SignedJWT verifyToken(String token,boolean isRefrseh) throws JOSEException, ParseException {
        JWSVerifier verifier= new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT=SignedJWT.parse(token);

        Date expityTime=(isRefrseh)
                ?new Date(signedJWT.getJWTClaimsSet().getIssueTime().toInstant().plus(REFRESH_DURATION,ChronoUnit.SECONDS).toEpochMilli())
                :signedJWT.getJWTClaimsSet().getExpirationTime();

        var verify= signedJWT.verify(verifier);

       if(!(verify && expityTime.after(new Date())))
           throw new AppException(ErrorCode.UNAUTHENTICATED);

       if(invalidtedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
           throw new AppException(ErrorCode.UNAUTHENTICATED);

        return signedJWT;

    }

    private String generateToken(User user){
       JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(user.getFullName())
                .issuer("devteira.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(VALIDATION_DURATION, ChronoUnit.SECONDS).toEpochMilli()
                ))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope",buildScope(user))
                .claim("userId", user.getId())
                .build();

        Payload payload = new Payload(claimsSet.toJSONObject());

        JWSObject jwsObject=new JWSObject(header,payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return  jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new RuntimeException(e);

        }
    }

    private String buildScope(User user){
        StringJoiner stringJoiner=new StringJoiner(" ");
        if(!CollectionUtils.isEmpty(user.getRoles()))
            user.getRoles().forEach(role->{
                stringJoiner.add("ROLE_"+role.getNameRoles());
                if(CollectionUtils.isEmpty(role.getPermissions())){}
                  role.getPermissions().forEach(permission->stringJoiner.add(permission.getNamePermission()));
        });

        return stringJoiner.toString();
    }

}
