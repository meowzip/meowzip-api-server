package com.meowzip.apiserver.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meowzip.apiserver.global.filter.CustomUsernamePasswordAuthenticationFilter;
import com.meowzip.apiserver.global.filter.JwtFilter;
import com.meowzip.apiserver.global.handler.*;
import com.meowzip.apiserver.jwt.service.JwtService;
import com.meowzip.apiserver.member.service.AuthConst;
import com.meowzip.apiserver.member.service.CustomOAuth2UserService;
import com.meowzip.apiserver.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;

@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomLoginSuccessHandler loginSuccessHandler;
    private final CustomLoginFailureHandler loginFailureHandler;
    private final CustomLogoutHandler logoutHandler;
    private final CustomLogoutSuccessHandler logoutSuccessHandler;
    private final MemberService memberService;
    private final JwtService jwtService;
    private final ObjectMapper objectMapper;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final OAuth2FailureHandler oAuth2FailureHandler;
    private final CustomOAuth2UserService oAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, CustomUsernamePasswordAuthenticationFilter customUsernamePasswordAuthenticationFilter) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> {
                    cors.configurationSource(request -> {
                        var corsConfiguration = new CorsConfiguration();
                        corsConfiguration.addAllowedOriginPattern("http://localhost:3000");
                        corsConfiguration.addAllowedOriginPattern("http://localhost:8080");
                        corsConfiguration.addAllowedOriginPattern("https://dev.meowzip.com");
                        corsConfiguration.addAllowedOriginPattern("https://dev.meowzip.com/api/auth/callback/google");
                        corsConfiguration.addAllowedOriginPattern("https://meowzip-front.vercel.app");
                        corsConfiguration.addAllowedHeader("*");
                        corsConfiguration.addAllowedMethod("*");
                        corsConfiguration.setAllowCredentials(true);

                        corsConfiguration.addExposedHeader(AuthConst.ACCESS_TOKEN_HEADER_NAME);
                        corsConfiguration.addExposedHeader("Set-Cookie");

                        return corsConfiguration;
                    });
                })
                .sessionManagement(sessionManagement -> {
                    sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .exceptionHandling(exceptionHandling -> {
                    exceptionHandling
                            .authenticationEntryPoint(authenticationEntryPoint)
                            .accessDeniedHandler(accessDeniedHandler)
                    ;
                })
                .authorizeHttpRequests(authorizeHttpRequests -> {
                    authorizeHttpRequests
                            .requestMatchers("/error").permitAll()
                            .requestMatchers("/h2-console/**").permitAll()
                            .requestMatchers("/favicon.co", "/favicon.ico").permitAll()
                            .requestMatchers("/**/api-docs/**", "/swagger-ui/**", "/swagger-resources/**").permitAll()
                            .requestMatchers("/health-check").permitAll()
                            .requestMatchers("/swagger-ui.html").permitAll()
                            .requestMatchers("/api/public/**").permitAll()
                            .anyRequest().authenticated();
                })
                .formLogin(AbstractHttpConfigurer::disable)
                .oauth2Login(configurer -> {
                    configurer.successHandler(oAuth2SuccessHandler);
                    configurer.failureHandler(oAuth2FailureHandler);
                    configurer.userInfoEndpoint(userInfoEndpointConfig -> {
                        userInfoEndpointConfig.userService(oAuth2UserService);
                    });
                })
                .logout(logout -> {
                    logout
                            .addLogoutHandler(logoutHandler)
                            .logoutUrl("/api/public/v1.0.0/members/logout")
                            .logoutSuccessHandler(logoutSuccessHandler)
                    ;
                })
        ;

        http.addFilterBefore(new JwtFilter(jwtService), UsernamePasswordAuthenticationFilter.class);
        http.addFilterAfter(customUsernamePasswordAuthenticationFilter, LogoutFilter.class);

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return memberService;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public CustomUsernamePasswordAuthenticationFilter customUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager) throws Exception {
        var filter = new CustomUsernamePasswordAuthenticationFilter(objectMapper);
        filter.setAuthenticationManager(authenticationManager);
        filter.setAuthenticationSuccessHandler(loginSuccessHandler);
        filter.setAuthenticationFailureHandler(loginFailureHandler);

        return filter;
    }
}
