package br.com.marcoshssilva.springadminserver.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

public final class JwtTokenResponse implements Serializable {
    private @JsonProperty("access_token") String accessToken;
    private @JsonProperty("token_type") String tokenType;
    private @JsonProperty("expires_in") Long expiresIn;

    public JwtTokenResponse(String accessToken, String tokenType, Long expiresIn) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
    }

    public JwtTokenResponse() { }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JwtTokenResponse that = (JwtTokenResponse) o;
        return Objects.equals(accessToken, that.accessToken) && Objects.equals(tokenType, that.tokenType) && Objects.equals(expiresIn, that.expiresIn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accessToken, tokenType, expiresIn);
    }

    @Override
    public String toString() {
        return "{" + "accessToken='" + accessToken + '\'' + ", tokenType='" + tokenType + '\'' + ", expiresIn=" + expiresIn + '}';
    }
}
