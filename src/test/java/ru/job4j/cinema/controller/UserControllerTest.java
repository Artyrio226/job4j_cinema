package ru.job4j.cinema.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.user.UserService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserControllerTest {
    private UserService userService;
    private UserController userController;

    @BeforeEach
    public void initServices() {
        userService = mock(UserService.class);
        userController = new UserController(userService);
    }

    /**
     * Тест на метод getRegistrationPage().
     * При обращении возвращает страницу регистрации пользователей.
     */
    @Test
    public void whenGetRegisterPageThenGetRegisterPage() {
        var view = userController.getRegistrationPage();
        assertThat(view).isEqualTo("users/register");
    }

    /**
     * Тест на метод register().
     * При регистрации нового пользователя с уникальной почтой, пользователь сохраняется в БД,
     * клиент переходит на страницу sessions.
     */
    @Test
    public void whenRequestToRegisterUserThenUserSavedAndGetSessionsPage() {
        var user = new User(1, "name", "user@email", "password");
        var userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        when(userService.save(userArgumentCaptor.capture())).thenReturn(Optional.of(user));

        var model = new ConcurrentModel();
        var view = userController.register(model, user);
        var actualUser = userArgumentCaptor.getValue();

        assertThat(view).isEqualTo("redirect:/sessions");
        assertThat(actualUser).isEqualTo(user);
    }

    /**
     * Тест на метод register().
     * При регистрации нового пользователя с неуникальной почтой, выбрасывается исключение,
     * клиент переходит на страницу errors/404, где отображается сообщение об ошибке.
     */
    @Test
    public void whenRegisterUserThenEmptyAndGetErrorPageWithMessage() {
        var model = new ConcurrentModel();
        when(userService.save(any(User.class))).thenReturn(Optional.empty());

        var view = userController.register(model, new User());
        var actualMessage = model.getAttribute("error");

        assertThat(view).isEqualTo("users/register");
        assertThat(actualMessage).isEqualTo("Пользователь с такой почтой уже существует");
    }

    /**
     * Тест на метод getLoginPage().
     * При обращении возвращает страницу логирования.
     */
    @Test
    public void whenGetLoginPageThenLoginPage() {
        var view = userController.getLoginPage();
        assertThat(view).isEqualTo("users/login");
    }

    /**
     * Тест на метод loginUser().
     * Клиент обращается к странице /users/login и вводит корректные данные,
     * после чего он входит в систему и его переносит на sessions
     */
    @Test
    public void whenLoginUserThenRedirectSessions() {
        var model = new ConcurrentModel();
        var user = new User(1, "test", "test@mail.ru", "123");

        var emailArgumentCaptor = ArgumentCaptor.forClass(String.class);
        var passwordArgumentCaptor = ArgumentCaptor.forClass(String.class);
        when(userService.findByEmailAndPassword(emailArgumentCaptor.capture(),
                passwordArgumentCaptor.capture())).thenReturn(Optional.of(user));

        var view = userController.loginUser(user, model, new MockHttpServletRequest());
        var actualEmail = emailArgumentCaptor.getValue();
        var actualPassword = passwordArgumentCaptor.getValue();

        assertThat(view).isEqualTo("redirect:/sessions");
        assertThat(actualEmail).isEqualTo("test@mail.ru");
        assertThat(actualPassword).isEqualTo("123");
    }

    /**
     * Тест на метод loginUser().
     * Клиент обращается к странице /users/login и вводит некорректные данные,
     * после чего выводится сообщение "Почта или пароль введены неверно"
     */
    @Test
    public void whenLoginUserThenEmptyAndGetMessage() {
        var model = new ConcurrentModel();
        when(userService.findByEmailAndPassword(any(String.class), any(String.class)))
                .thenReturn(Optional.empty());

        var view = userController.loginUser(new User(), model, new MockHttpServletRequest());
        var actualMessage = model.getAttribute("error");

        assertThat(view).isEqualTo("users/login");
        assertThat(actualMessage).isEqualTo("Почта или пароль введены неверно");
    }

    /**
     * Тест на метод logout().
     * При обращении возвращает страницу логирования.
     */
    @Test
    public void whenRequestToLogoutPageThenGetIt() {
        assertThat(userController.getLoginPage()).isEqualTo("users/login");
    }
}