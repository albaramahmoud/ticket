package com.albara.ticket.filters;

import com.albara.ticket.domain.entities.User;
import com.albara.ticket.repositories.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.io.IOException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserProvisioningFilterTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private Jwt jwt;

    @InjectMocks
    private UserProvisioningFilter filter;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void shouldProvisionNewUserWhenAuthenticatedAndUserDoesNotExist() throws ServletException, IOException {
        // Arrange
        String mockUserId = UUID.randomUUID().toString();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(jwt);
        when(jwt.getSubject()).thenReturn(mockUserId);
        when(jwt.getClaimAsString("preferred_username")).thenReturn("albara");
        when(jwt.getClaimAsString("email")).thenReturn("albara@test.com");

        when(userRepository.existsById(UUID.fromString(mockUserId))).thenReturn(false);

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertThat(savedUser.getId().toString()).isEqualTo(mockUserId);
        assertThat(savedUser.getName()).isEqualTo("albara");
        assertThat(savedUser.getEmail()).isEqualTo("albara@test.com");

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldNotProvisionUserWhenAlreadyExists() throws ServletException, IOException {
        // Arrange
        String mockUserId = UUID.randomUUID().toString();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(jwt);
        when(jwt.getSubject()).thenReturn(mockUserId);

        when(userRepository.existsById(UUID.fromString(mockUserId))).thenReturn(true);

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(userRepository, never()).save(any(User.class));
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldDoNothingWhenNotAuthenticated() throws ServletException, IOException {
        // Arrange
        when(securityContext.getAuthentication()).thenReturn(null);

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(userRepository, never()).existsById(any());
        verify(userRepository, never()).save(any());
        verify(filterChain).doFilter(request, response);
    }
}
