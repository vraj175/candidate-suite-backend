package com.aspire.kgp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableAuthorizationServer
public class OAuth2Config extends AuthorizationServerConfigurerAdapter {
  @Value("${spring.security.oauth2.client-id}")
  private String clientID;

  @Value("${spring.security.oauth2.client-secret}")
  private String clientSecret;

  @Value("${spring.security.oauth2.accessTokenValidity}")
  private int accessTokenValidity;

  @Value("${spring.security.oauth2.refreshTokenValidity}")
  private int refreshTokenValidity;

  @Autowired
  @Qualifier("authenticationManagerBean")
  private AuthenticationManager authenticationManager;

  @Autowired
  private BCryptPasswordEncoder passwordEncoder;

  private String privateKey =
      "MIIEpAIBAAKCAQEAuRz9wUJs6DLiDfDdO3B1stEzw1yRZYmp5TXmvGtDonR4yAltQ0nhOSCPBzeZRcVI1Xvan7I2VXS0E8MP2nzkVK51ASHIDQy5NEoP9P4cNQe8aE9jI0qFLINjMh/J8LyhH8EKdUvBp77sQjCBQfBRzVOYz8fxxMrfnBkaQhMXXpmA1x1b6Z2wExdeUYGK915WfIx59TsiVkWaKZ49/TwNP3qhEEtX8axipT7I0roBoOw5JCo2pcKX7c+4rOQm6JVbCU3qd7JwZ2LTNLaROzanZ13Nloq2pS87kdy0E7w0bOdWu/S9YgULuxZ2UjuP+1gyeCihlxMToFOPuHY/8B6pdQIDAQABAoIBABlbKEMdpy9TMPW55YPrzqN01oNPHbdr2PLFpFpw0u3gyF6WM/pAl3IszIqvifYtpOFhOrwfPCQOfVArqvNBokEbOHm0N+PZt1C7SF5FpHV7Hsqm/SxrW7ySLrdB9XZdUDC6T4VroBYrJ8+VN09h7MKUTd1ARRSwuVp3ccpVNSFG548dCkFSMWHZ65Voj1DnBmLJSUUrpsPkbOYzsLfBo7JYhvmWlG0mdJt1e0UMX23PAd/dLZIcBoAKjxKnbUXUynkpGMCONDZtZ/Mf/uOaE6W/2zdJprvKBUZBMOSKmfMcdOPWiNhZ2KLmHSiNq3qYR4P8rlNd97BqymocU5u3TQ0CgYEA8gOaQ9TXNGFp7X8DEqgdWx0Bxc3E4JrWaIEW9PKMRiAGndKNF02GHr67srP+DfHNiOz5DZQlT+RtyDKXHFFDPJwMhimgLvlZyI1IPoyYGtgYgr7am/zNjOpEh8KKZA84dIOa+tyDIC2wssC8aQwIr052VqzRmD9jLmZYkGnrQqcCgYEAw8+Wj5ZCfCYpgdZ0Lt1Cp/5zHkhYOtgPcFJNxKbPDGRQ7V13lg7r9EhS7arZmKbPE7dY7gBQ3bjgYcVfb+fhYZYiDcPKxO8HsMfincBNEWX2leVhSBoPAjsohgpQoIz0CcPm/JYHH/l07vsv0OP3tFGzXgx/Kc7jWkBUGs4fwoMCgYEAv4t2Z0Vn5r9K9XvXVMbWJSz6IDJSJmqmVCQHpWrq9v2V/weMP+tNwbu4FyI6a5L71+mm3MZqGadz9EWGMBVpH+lqSC1d6I/WLdMUYN0p7eUm0SR5JYiar3MjNkgU/EjHqAAZg39QEQHYnkHJXWL25Lk3FDbH1LYmq6y5X+NVHnUCgYB2Y2RcnDTGayxdXBQS4aQTfjyL6qGtskRjXN+4LkKw81YarZBIIe13XOMG322AZhp9iTUzcO1zoPvJIVR6jHAwWF1uhRprxzmXIMNbLZFrG4LjTjRrK5Y3ex0dLc9hwtcpsFBQF0Vnh6OzehuG6M4zL9KK4nNgC75aKtqu+kEpkQKBgQC8xugCrfHe3tHXVHnAxBhB+etBcmprqvq8VC+kOAoPXiaU92Or/JVA+ySLdUY053IbZKhw3saQOlq29F7W2reE+QDMFlM52MMu5hV3nMsh5kRZBJi+nITVtuc1VNcAAuW/Q+QhYqNPLLupozumvYGMfmCo2KbCTG3VACrDqST3gA==";
  private String publicKey =
      "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAuRz9wUJs6DLiDfDdO3B1stEzw1yRZYmp5TXmvGtDonR4yAltQ0nhOSCPBzeZRcVI1Xvan7I2VXS0E8MP2nzkVK51ASHIDQy5NEoP9P4cNQe8aE9jI0qFLINjMh/J8LyhH8EKdUvBp77sQjCBQfBRzVOYz8fxxMrfnBkaQhMXXpmA1x1b6Z2wExdeUYGK915WfIx59TsiVkWaKZ49/TwNP3qhEEtX8axipT7I0roBoOw5JCo2pcKX7c+4rOQm6JVbCU3qd7JwZ2LTNLaROzanZ13Nloq2pS87kdy0E7w0bOdWu/S9YgULuxZ2UjuP+1gyeCihlxMToFOPuHY/8B6pdQIDAQAB";

  @Bean
  public JwtAccessTokenConverter tokenEnhancer() {
    JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
    converter.setVerifierKey(publicKey);
    converter.setSigningKey(privateKey);
    return converter;
  }

  @Bean
  public JwtTokenStore tokenStore() {
    return new JwtTokenStore(tokenEnhancer());
  }

  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
    endpoints.authenticationManager(authenticationManager).tokenStore(tokenStore())
        .accessTokenConverter(tokenEnhancer());
  }

  @Override
  public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
    oauthServer.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()")
        .allowFormAuthenticationForClients();
  }

  public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    clients.inMemory().withClient(clientID).secret(passwordEncoder.encode(clientSecret))
        .authorizedGrantTypes("password", "authorization_code", "refresh_token").scopes("user_info")
        .authorities("READ_ONLY_CLIENT").accessTokenValiditySeconds(accessTokenValidity)
        .refreshTokenValiditySeconds(refreshTokenValidity);
  }
}
