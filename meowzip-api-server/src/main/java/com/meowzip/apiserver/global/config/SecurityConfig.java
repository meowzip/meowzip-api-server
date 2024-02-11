package com.meowzip.apiserver.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meowzip.apiserver.global.discord.service.DiscordService;
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
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
    private final PasswordEncoder passwordEncoder;
    private final MemberService memberService;
    private final JwtService jwtService;
    private final DiscordService discordService;
    private final ObjectMapper objectMapper;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final OAuth2FailureHandler oAuth2FailureHandler;
    private final CustomOAuth2UserService oAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> {
                    cors.configurationSource(request -> {
                        var corsConfiguration = new CorsConfiguration();
                        corsConfiguration.addAllowedOriginPattern("*");
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

        http.addFilterAfter(customUsernamePasswordAuthenticationFilter(), LogoutFilter.class);
        http.addFilterBefore(new JwtFilter(jwtService, discordService), CustomUsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        var provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(memberService);

        return new ProviderManager(provider);
    }

    // TODO: @Bean annotation?
    public CustomUsernamePasswordAuthenticationFilter customUsernamePasswordAuthenticationFilter() throws Exception {
        var filter = new CustomUsernamePasswordAuthenticationFilter(objectMapper);
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(loginSuccessHandler);
        filter.setAuthenticationFailureHandler(loginFailureHandler);

        return filter;
    }
}
