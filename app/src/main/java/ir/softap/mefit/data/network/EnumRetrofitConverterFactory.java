package ir.softap.mefit.data.network;


import androidx.annotation.Nullable;
import com.google.gson.annotations.SerializedName;
import ir.softap.mefit.di.scope.ApplicationScope;
import retrofit2.Converter;
import retrofit2.Retrofit;

import javax.inject.Inject;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@ApplicationScope
public class EnumRetrofitConverterFactory extends Converter.Factory {

    @Inject
    public EnumRetrofitConverterFactory() {
    }

    @Override
    public Converter<?, String> stringConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        Converter<?, String> converter = null;
        if (type instanceof Class && ((Class<?>) type).isEnum()) {
            converter = value -> getSerializedNameValue((Enum) value);
        }
        return converter;
    }

    @Nullable
    public static <E extends Enum<E>> String getSerializedNameValue(E e) {
        String value = null;
        try {
            value = e.getClass().getField(e.name()).getAnnotation(SerializedName.class).value();
        } catch (NoSuchFieldException exception) {
            exception.printStackTrace();
        }
        return value;
    }
}
