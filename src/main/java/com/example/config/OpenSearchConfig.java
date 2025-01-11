package com.example.config;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.opensearch.client.RestClient;
import org.opensearch.client.RestClientBuilder;
import org.opensearch.client.json.jackson.JacksonJsonpMapper;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.transport.rest_client.RestClientTransport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;

/**
 *  OpenSearch接続設定を提供するクラス
 */
@Configuration
public class OpenSearchConfig {

    /**
     * openSearchクラスターのエンドポイント
     */
    @Value("${opensearch.endpoint}")
    private String openSearchEndpoint;

    /**
     * マスターのユーザー名
     */
    @Value("${opensearch.username}")
    private String username;

    /**
     * 上記ユーザーのパスワード
     */
    @Value("${opensearch.password}")
    private String password;
    
    @Bean
    public JacksonJsonpMapper jacksonJsonpMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        
        return new JacksonJsonpMapper(mapper);
    }

    /**
     * OpenSearchクライアントを生成
     * @return OpenSearchClientインスタンス
     */
    @Bean
    public OpenSearchClient openSearchClient(JacksonJsonpMapper jacksonJsonpMapper) {
     // Basic 認証設定を行うための CredentialsProvider を作成
        BasicCredentialsProvider provider = new BasicCredentialsProvider();
        provider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username,password));
        
     // RestClientBuilder に認証情報をセットしてビルド
        RestClientBuilder builder = RestClient.builder(new HttpHost(openSearchEndpoint, 443, "https"))
                .setHttpClientConfigCallback(httpBuilder -> {
                    return httpBuilder.setDefaultCredentialsProvider(provider);
                });
        
        // 上記 builder から RestClient を生成
        RestClient restClient = builder.build();

        RestClientTransport transport = new RestClientTransport(restClient, jacksonJsonpMapper);

        return new OpenSearchClient(transport);
    }
}
