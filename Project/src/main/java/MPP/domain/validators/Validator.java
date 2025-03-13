package MPP.domain.validators;

import MPP.exceptions.ValidationException;

public interface Validator<T> {
    void validate(T entity) throws ValidationException;
}
