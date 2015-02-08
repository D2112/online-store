package com.epam.store.model;

public class Date extends BaseEntity {
    private Long time;

    public Date() {
    }

    public Date(Long time) {
        this.time = time;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Date date = (Date) o;

        if (time != null ? !time.equals(date.time) : date.time != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return time != null ? time.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Date{" +
                "time=" + time +
                '}';
    }
}
