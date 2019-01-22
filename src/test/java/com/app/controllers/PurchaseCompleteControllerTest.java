package com.app.controllers;

import com.app.converters.BundleToOderHistory;
import com.app.domain.*;
import com.app.services.BundleService;
import com.app.services.RoleService;
import com.app.services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class PurchaseCompleteControllerTest {
    
    @InjectMocks
    private PurchaseCompleteController purchaseCompleteController;

    @Mock
    UserService userService;

    @Mock
    RoleService roleService;

    @Mock
    BundleService bundleService;

    @Mock
    BundleToOderHistory bundleToOderHistory;

    @Mock
    private Principal principal;

    @Mock
    private HttpSession session;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void whenSinglePurchase_thenReturnThankYouPage() {
        String expectedView = "thankyou";
        String actualView = purchaseCompleteController.singlePurchase();
        assertThat(actualView).isEqualTo(expectedView);
    }

    @Test
    public void givenGuestUserIsInDb_whenPayAsGuestUser_thenAddCartDetailsOrderHistoryOfUser() {
        User user = mock(User.class);
        user.setId(1);
        user.setUserName("guest");
        Product product = new Product();
        product.setId(1);
        product.setName("name");
        product.setPrice(BigDecimal.valueOf(10));
        CartDetail cartDetail = new CartDetail();
        cartDetail.setProduct(product);
        Cart cart = new Cart();
        cart.setId(1);
        cart.setCartDetails(Arrays.asList(cartDetail));
        when(userService.findByUserName(anyString())).thenReturn(user);
        when(session.getAttribute("cart")).thenReturn(cart);
        String expectedView = "thankyou";

        String actualView = purchaseCompleteController.payAsGuest(user, session);

        assertThat(actualView).isEqualTo(expectedView);
        verify(session, times(1)).getAttribute("cart");
        verify(user, times(1)).addOrderHistory(any(OrderHistory.class));
        verify(userService, times(1)).saveOrUpdate(user);
        verify(session, times(1)).removeAttribute("cart");
        verifyNoMoreInteractions(session);
    }

    @Test
    public void whenPayAsGuestUser_thenFetchAndAssignRoleToUserAndAddCartDetailsOrderHistoryOfUser() {
        User user = mock(User.class);
        user.setId(1);
        user.setUserName("user");
        user.setEmail("email");
        Product product = new Product();
        product.setId(1);
        product.setName("name");
        product.setPrice(BigDecimal.valueOf(10));
        CartDetail cartDetail = new CartDetail();
        cartDetail.setProduct(product);
        Cart cart = new Cart();
        cart.setId(1);
        cart.setCartDetails(Arrays.asList(cartDetail));
        Role role = new Role();
        role.setRole("CUSTOMER");
        role.setId(3);
        when(userService.findByUserName(anyString())).thenReturn(user);
        when(roleService.getById(3)).thenReturn(role);
        when(session.getAttribute("cart")).thenReturn(cart);
        String expectedView = "thankyou";

        String actualView = purchaseCompleteController.payAsGuest(user, session);

        assertThat(actualView).isEqualTo(expectedView);
        verify(roleService, times(1)).getById(3);
        verify(user, times(1)).getEmail();
        verify(user, times(1)).addRole(role);
        verify(session, times(1)).getAttribute("cart");
        verify(user, times(1)).addOrderHistory(any(OrderHistory.class));
        verify(userService, times(1)).saveOrUpdate(user);
        verify(session, times(1)).removeAttribute("cart");
        verifyNoMoreInteractions(session);
    }

    
}
