package com.example.sslclient;

import com.example.sslclient.support.CustomConnectionKeepAliveStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11Nio2Protocol;
import org.apache.coyote.http11.Http11NioProtocol;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 *
 */
@SpringBootApplication
@Slf4j
public class SslClientApplication {

	@Value("${security.key-store}")
	private Resource keyStore;
	@Value("${security.key-pass}")
	private String keyPass;

	public static void main(String[] args) {
		new SpringApplicationBuilder()
				.sources(SslClientApplication.class)
                .bannerMode(Banner.Mode.OFF)
                .web(WebApplicationType.SERVLET)
				.run(args);//
	}

	@Bean
	public HttpComponentsClientHttpRequestFactory requestFactory() {
		SSLContext sslContext = null;
		try {
			sslContext = SSLContextBuilder.create()
					.setKeyStoreType("PKCS12")
					// 会校验证书
					.loadTrustMaterial(keyStore.getURL(), keyPass.toCharArray())
					// 放过所有证书校验
//					.loadTrustMaterial(null, (certificate, authType) -> true)
					.build();
		} catch(Exception e) {
			log.error("Exception occurred while creating SSLContext.", e);
		}

		CloseableHttpClient httpClient = HttpClients.custom()
				.evictIdleConnections(30, TimeUnit.SECONDS)
				.setMaxConnTotal(200)
				.setMaxConnPerRoute(20)
				.disableAutomaticRetries()
				.setKeepAliveStrategy(new CustomConnectionKeepAliveStrategy())
				.setSSLContext(sslContext)
				.setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
				.build();

		HttpComponentsClientHttpRequestFactory requestFactory =new HttpComponentsClientHttpRequestFactory(httpClient);

		return requestFactory;
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder
				.setConnectTimeout(Duration.ofMillis(100))
				.setReadTimeout(Duration.ofMillis(500))
				.requestFactory(this::requestFactory)
				.build();
	}

	@Bean
	public ServletWebServerFactory servletContainer() {
		TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
		tomcat.addAdditionalTomcatConnectors(createSslConnector());
		return tomcat;
	}

	private Connector createSslConnector() {
		Connector connector = new Connector("org.apache.coyote.http11.Http11Nio2Protocol");
		Http11Nio2Protocol protocol = (Http11Nio2Protocol) connector.getProtocolHandler();
		try {
			//File keystore = new ClassPathResource("keystore").getFile();
			//File truststore = new ClassPathResource("keystore").getFile();
			connector.setScheme("http");
			connector.setSecure(true);
			connector.setPort(5001);
			protocol.setSSLEnabled(false);
			//protocol.setKeystoreFile(keystore.getAbsolutePath());
			//protocol.setKeystorePass("changeit");
			//protocol.setTruststoreFile(truststore.getAbsolutePath());
			//protocol.setTruststorePass("changeit");
			//protocol.setKeyAlias("apitester");
			return connector;
		}
		catch (Exception ex) {
			throw new IllegalStateException("can't access keystore: [" + "keystore"
					+ "] or truststore: [" + "keystore" + "]", ex);
		}
	}



}
