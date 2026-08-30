package com.tech.erp.identity.infrastructure.web;

import com.tech.erp.identity.application.DuplicatePermissionCodeException;
import com.tech.erp.identity.application.DuplicateRoleCodeException;
import com.tech.erp.identity.application.EmailAlreadyUsedException;
import com.tech.erp.identity.application.PermissionNotFoundException;
import com.tech.erp.identity.application.RoleNotFoundException;
import com.tech.erp.identity.application.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/** Maps Identity domain/application failures to HTTP, scoped to this module's controllers. */
@RestControllerAdvice(basePackageClasses = UserController.class)
class IdentityExceptionHandler {

    @ExceptionHandler({
            EmailAlreadyUsedException.class,
            DuplicatePermissionCodeException.class,
            DuplicateRoleCodeException.class})
    ProblemDetail conflict(RuntimeException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler({
            UserNotFoundException.class,
            PermissionNotFoundException.class,
            RoleNotFoundException.class})
    ProblemDetail notFound(RuntimeException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ProblemDetail badRequest(RuntimeException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
}
