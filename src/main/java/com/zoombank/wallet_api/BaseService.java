package com.zoombank.wallet_api;

/**
 * Represent service stereotype for ID base entity
 * @param <T>
 */
public abstract class BaseService<T> {

    private int idNumber = 0;

    public abstract T create(T target);

    protected String createID() {
        this.idNumber += 1;
        return String.format("%s%08d",this.preFix(), this.idNumber);
    }

    protected abstract String preFix();

}
