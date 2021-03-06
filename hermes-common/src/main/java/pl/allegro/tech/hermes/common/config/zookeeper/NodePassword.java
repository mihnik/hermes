package pl.allegro.tech.hermes.common.config.zookeeper;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;

import java.util.Arrays;

public class NodePassword {

    private static final int DEFAULT_STRING_LENGTH = 8;

    private final byte[] hashedPassword;

    @JsonCreator
    public NodePassword(@JsonProperty("hashedPassword") byte[] hashedPassword) {
        this.hashedPassword = Arrays.copyOf(hashedPassword, hashedPassword.length);
    }

    public NodePassword(String password) {
        this.hashedPassword = NodePassword.hashString(password);
    }

    public byte[] getHashedPassword() {
        return Arrays.copyOf(hashedPassword, hashedPassword.length);
    }

    public boolean matches(String password) {
        return this.equals(NodePassword.fromString(password));
    }

    private static byte[] hashString(String string) {
        return DigestUtils.md5(string);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null) {
            return false;
        }

        if (o instanceof String) {
            return this.equals(NodePassword.fromString((String) o));
        }

        if (getClass() != o.getClass()) {
            return false;
        }

        NodePassword that = (NodePassword) o;

        return Arrays.equals(hashedPassword, that.hashedPassword);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(hashedPassword);
    }

    public static NodePassword fromString(String string) {
        return new NodePassword(string);
    }

    public static String getRandomPassword() {
        return RandomStringUtils.randomAlphanumeric(DEFAULT_STRING_LENGTH);
    }

}
