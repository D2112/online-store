package com.epam.store.model;

public class Password extends BaseEntity {
    private String hash;
    private String salt;

    public Password() {
    }

    public Password(String hash, String salt) {
        this.hash = hash;
        this.salt = salt;
    }

    public String getHash() {
        return hash;
    }

    public String getSalt() {
        return salt;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Password password = (Password) o;

        if (hash != null ? !hash.equals(password.hash) : password.hash != null) return false;
        if (salt != null ? !salt.equals(password.salt) : password.salt != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = hash != null ? hash.hashCode() : 0;
        result = 31 * result + (salt != null ? salt.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Password{" +
                "hash='" + hash + '\'' +
                ", salt='" + salt + '\'' +
                '}';
    }
}
