package ptit.edu.vn.bookshop.service;

import ptit.edu.vn.bookshop.domain.dto.request.auth.RegisterRequestDTO;

public interface RegisterService {
    String userRegister(RegisterRequestDTO registerRequestDTO);
    String verifyUser(String token);
}
