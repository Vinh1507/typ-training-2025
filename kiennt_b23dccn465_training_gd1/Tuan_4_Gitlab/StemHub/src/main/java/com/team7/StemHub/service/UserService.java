package com.team7.StemHub.service;

import com.team7.StemHub.dao.DocumentRepo;
import com.team7.StemHub.dao.UserRepo;
import com.team7.StemHub.exception.NotFoundException;
import com.team7.StemHub.model.Document;
import com.team7.StemHub.model.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;
    private final DocumentRepo documentRepo;

    public List<User> getTop10UsersOrderByDocument() {
        return userRepo.findTop10OrderByDocumentNumberDesc();
    }

    public User getUserByIdWithUploadFile(UUID id){
        return userRepo.findByIdWithUploads(id).orElseThrow(() -> new NotFoundException("User not found with id: " + id));
    }

    public User getUserById(UUID id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));
    }

    public User findByUsername(String username) {
        return userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }

    @Transactional
    public boolean likeDocument(UUID userId, UUID documentId) {
        User user = userRepo.findByIdWithFavorites(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));
        Document document = documentRepo.getReferenceById(documentId);
        Set<Document> favorites = user.getFavoritesDocuments();
        boolean alreadyLiked = favorites.stream().anyMatch(d -> d.getId().equals(documentId));
        if (alreadyLiked) {
            favorites.removeIf(d -> d.getId().equals(documentId));
            return false; // now unfavorited
        } else {
            favorites.add(document);
            return true; // now favorited
        }
    }

    public Page<User> searchUsers(String keyword, int page) {
        Pageable pageRequest = PageRequest.of(page , 3);
        return userRepo.searchByFullnameOrUsername(keyword, pageRequest);
    }

    public User findByUsernameWithAllData(String username) {
        return userRepo.findByUsernameWithAllData(username)
                .orElseThrow(() -> new NotFoundException("User not found: " + username));
    }
}
