package cn.gdrfgdrf.smartuploader.utils;

import lombok.Getter;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Copy from io.netty.util.internal.TypeParameterMatcher of Netty. (Changed)
 *
 */
public abstract class TypeParameterMatcher {
    /**
     * This code was copied from io.netty.util.internal.InternalThreadLocalMap of Netty. (Changed)
     */
    //=================================================================================================
    private static Map<Class<?>, Map<String, TypeParameterMatcher>> typeParameterMatcherFindCache;
    private static Map<Class<?>, TypeParameterMatcher> typeParameterMatcherGetCache;

    private static Map<Class<?>, Map<String, TypeParameterMatcher>> typeParameterMatcherFindCache() {
        Map<Class<?>, Map<String, TypeParameterMatcher>> cache = typeParameterMatcherFindCache;
        if (cache == null) {
            typeParameterMatcherFindCache = cache = new IdentityHashMap<>();
        }
        return cache;
    }

    private static Map<Class<?>, TypeParameterMatcher> typeParameterMatcherGetCache() {
        Map<Class<?>, TypeParameterMatcher> cache = typeParameterMatcherGetCache;
        if (cache == null) {
            typeParameterMatcherGetCache = cache = new IdentityHashMap<>();
        }
        return cache;
    }
    //=================================================================================================

    private static final TypeParameterMatcher NOOP = new TypeParameterMatcher() {
        @Override
        public Class<?> getType() {
            return Object.class;
        }

        @Override
        public boolean match(Object msg) {
            return true;
        }
    };

    public static TypeParameterMatcher get(final Class<?> parameterType) {
        final Map<Class<?>, TypeParameterMatcher> getCache =
                typeParameterMatcherGetCache();

        TypeParameterMatcher matcher = getCache.get(parameterType);
        if (matcher == null) {
            if (parameterType == Object.class) {
                matcher = NOOP;
            } else {
                matcher = new ReflectiveMatcher(parameterType);
            }
            getCache.put(parameterType, matcher);
        }

        return matcher;
    }

    public static TypeParameterMatcher find(
            final Object object, final Class<?> parametrizedSuperclass, final String typeParamName) {

        final Map<Class<?>, Map<String, TypeParameterMatcher>> findCache =
                typeParameterMatcherFindCache();
        final Class<?> thisClass = object.getClass();

        Map<String, TypeParameterMatcher> map = findCache.computeIfAbsent(thisClass, k -> new HashMap<>());

        TypeParameterMatcher matcher = map.get(typeParamName);
        if (matcher == null) {
            matcher = get(find0(object, parametrizedSuperclass, typeParamName));
            map.put(typeParamName, matcher);
        }

        return matcher;
    }

    private static Class<?> find0(
            final Object object, Class<?> parametrizedSuperclass, String typeParamName) {

        final Class<?> thisClass = object.getClass();
        Class<?> currentClass = thisClass;
        for (;;) {
            if (currentClass.getSuperclass() == parametrizedSuperclass) {
                int typeParamIndex = getTypeParamIndex(parametrizedSuperclass, typeParamName, currentClass);

                Type genericSuperType = currentClass.getGenericSuperclass();
                if (!(genericSuperType instanceof ParameterizedType)) {
                    return Object.class;
                }

                Type[] actualTypeParams = ((ParameterizedType) genericSuperType).getActualTypeArguments();

                Type actualTypeParam = actualTypeParams[typeParamIndex];
                if (actualTypeParam instanceof ParameterizedType) {
                    actualTypeParam = ((ParameterizedType) actualTypeParam).getRawType();
                }
                if (actualTypeParam instanceof Class) {
                    return (Class<?>) actualTypeParam;
                }
                if (actualTypeParam instanceof GenericArrayType) {
                    Type componentType = ((GenericArrayType) actualTypeParam).getGenericComponentType();
                    if (componentType instanceof ParameterizedType) {
                        componentType = ((ParameterizedType) componentType).getRawType();
                    }
                    if (componentType instanceof Class) {
                        return Array.newInstance((Class<?>) componentType, 0).getClass();
                    }
                }
                if (actualTypeParam instanceof TypeVariable<?> v) {
                    // Resolved type parameter points to another type parameter.
                    if (!(v.getGenericDeclaration() instanceof Class)) {
                        return Object.class;
                    }

                    currentClass = thisClass;
                    parametrizedSuperclass = (Class<?>) v.getGenericDeclaration();
                    typeParamName = v.getName();
                    if (parametrizedSuperclass.isAssignableFrom(thisClass)) {
                        continue;
                    }
                    return Object.class;
                }

                return fail(thisClass, typeParamName);
            }
            currentClass = currentClass.getSuperclass();
            if (currentClass == null) {
                return fail(thisClass, typeParamName);
            }
        }
    }

    private static int getTypeParamIndex(Class<?> parametrizedSuperclass, String typeParamName, Class<?> currentClass) {
        int typeParamIndex = -1;
        TypeVariable<?>[] typeParams = currentClass.getSuperclass().getTypeParameters();
        for (int i = 0; i < typeParams.length; i ++) {
            if (typeParamName.equals(typeParams[i].getName())) {
                typeParamIndex = i;
                break;
            }
        }

        if (typeParamIndex < 0) {
            throw new IllegalStateException(
                    "unknown type parameter '" + typeParamName + "': " + parametrizedSuperclass);
        }
        return typeParamIndex;
    }

    private static Class<?> fail(Class<?> type, String typeParamName) {
        throw new IllegalStateException(
                "cannot determine the type of the type parameter '" + typeParamName + "': " + type);
    }

    public abstract Class<?> getType();
    public abstract boolean match(Object msg);

    @Getter
    private static final class ReflectiveMatcher extends TypeParameterMatcher {
        private final Class<?> type;

        ReflectiveMatcher(Class<?> type) {
            this.type = type;
        }

        @Override
        public boolean match(Object msg) {
            return type.isInstance(msg);
        }
    }

    TypeParameterMatcher() { }
}
