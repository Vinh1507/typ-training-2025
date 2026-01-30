package ptit.edu.vn.bookshop.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ptit.edu.vn.bookshop.domain.constant.BookStatusEnum;
import ptit.edu.vn.bookshop.domain.dto.request.AddCartItemRequestDTO;
import ptit.edu.vn.bookshop.domain.dto.response.CartResponseDTO;

import ptit.edu.vn.bookshop.domain.entity.Book;
import ptit.edu.vn.bookshop.domain.entity.Cart;
import ptit.edu.vn.bookshop.domain.entity.CartItem;
import ptit.edu.vn.bookshop.domain.entity.User;
import ptit.edu.vn.bookshop.exception.IdInvalidException;
import ptit.edu.vn.bookshop.exception.UsernameNotFoundException;
import ptit.edu.vn.bookshop.repository.*;
import ptit.edu.vn.bookshop.service.CartService;
import ptit.edu.vn.bookshop.service.UserService;
import ptit.edu.vn.bookshop.domain.dto.mapper.CartMapper;
import ptit.edu.vn.bookshop.util.security.SecurityUtil;

import java.util.ArrayList;
import java.util.Optional;


@Service
public class CartServiceImpl implements CartService {

    private final BookRepository bookRepository;
    private final CartRepository cartRepository;
    private final UserService userService;
    private final CartMapper cartMapper;
    private final CartItemRepository cartItemRepository;

    public CartServiceImpl(BookRepository bookRepository, CartRepository cartRepository, UserService userService, CartMapper cartMapper,
        CartItemRepository cartItemRepository) {
        this.bookRepository = bookRepository;
        this.cartRepository = cartRepository;
        this.userService = userService;
        this.cartMapper = cartMapper;
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public CartResponseDTO addItemToCart(AddCartItemRequestDTO addCartItemRequestDTO) {
        // lấy ra thông tin sách
        Book book = this.bookRepository.findById(addCartItemRequestDTO.getProductId())
                .orElseThrow(() -> new IdInvalidException("Book not found"));

        // kiểm tra trạng thái của sách
        if (book.getStatus() != BookStatusEnum.AVAILABLE) {
            throw new IllegalArgumentException("Book is not AVAILABLE");
        }

        // lấy ra thông tin user
        String email = SecurityUtil.getCurrentUserLogin()
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        User user = this.userService.getUserByUsername(email);

        // lấy giỏ hàng của user, nếu chưa có thì tạo mới
        Cart cart = this.cartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
//                    newCart.setStatus(CartStatusEnum.ACTIVE);
                    newCart.setCartItems(new ArrayList<>());
                    return this.cartRepository.save(newCart);
                });

        // kiểm tra sản phẩm đã có trong giỏ hàng chưa
        Optional<CartItem> cartItemOptional = cart.getCartItems()
                .stream()
                .filter(p -> p.getBook().getId().equals(book.getId()))
                .findFirst();

        CartItem cartItem;
        if (!cartItemOptional.isPresent()) {
            // nếu chưa có trong giỏ hàng, tạo mới
            int quantityToAdd = addCartItemRequestDTO.getQuantity();
            if (quantityToAdd > book.getQuantity()) {
                throw new IllegalArgumentException("Quantity exceeds available stock");
            }
            cartItem = new CartItem();
            cartItem.setBook(book);
            cartItem.setQuantity(quantityToAdd);
            cartItem.setUnitPrice(book.getPrice());
            cartItem.setItemDiscount(book.getDiscount());
            cartItem.setCart(cart);
            cart.getCartItems().add(cartItem);
        } else {
            cartItem = cartItemOptional.get();
            int newQuantity = cartItem.getQuantity() + addCartItemRequestDTO.getQuantity();
            if (newQuantity > book.getQuantity()) {
                throw new IllegalArgumentException("Quantity exceeds available stock");
            }
            cartItem.setQuantity(newQuantity);
            cartItem.setUnitPrice(book.getPrice());
            cartItem.setItemDiscount(book.getDiscount());
        }

        this.bookRepository.save(book);
        this.cartRepository.save(cart);

        return this.cartMapper.mapCartToResponseDTO(cart);
    }

    @Override
    public CartResponseDTO updateCart(Integer newQuantity, Long cartItemId) {
        // lấy thông tin user
        String email = SecurityUtil.getCurrentUserLogin()
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        User user = this.userService.getUserByUsername(email);

        // lấy cart của user
        Cart cart = this.cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        CartItem cartItem = cart.getCartItems().stream()
                .filter(it -> it.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found"));

        Book book = cartItem.getBook();
        // check số lượng tồn kho
        if(newQuantity > book.getQuantity()) {
            throw new IllegalArgumentException("Quantity exceeds available stock");
        }

        // update số lượng trong giỏ
        cartItem.setQuantity(newQuantity);

        this.cartRepository.save(cart);
        return this.cartMapper.mapCartToResponseDTO(cart);
    }

    @Override
    @Transactional
    public void removeCartItem(Long cartItemId) {
        String email = SecurityUtil.getCurrentUserLogin()
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        User user = this.userService.getUserByUsername(email);

        Cart cart = this.cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        int deleted = this.cartItemRepository.deleteByIdAndCartId(cartItemId, cart.getId());
        if (deleted == 0) {
            throw new IllegalArgumentException("Cart item not found");
        }
        this.cartItemRepository.saveAll(cart.getCartItems());
    }

    @Override
    public CartResponseDTO getCartItems() {
        // lấy thông tin user
        String email = SecurityUtil.getCurrentUserLogin()
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        User user = this.userService.getUserByUsername(email);

        // lấy cart của user
        Cart cart = this.cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        return this.cartMapper.mapCartToResponseDTO(cart);
    }

    @Override
    public Cart getCartByUser(Long id) {
       Cart cart = this.cartRepository.findByUserId(id).orElseThrow(() -> new IdInvalidException("Cart not found"));
       return cart;
    }

}
