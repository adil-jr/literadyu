package io.github.adil_jr.literadyu.service;

public interface IDataConverter {
    <T> T getData(String json, Class<T> targetClass);
}
