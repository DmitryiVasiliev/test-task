package com.haulmont.testtask.UI.Page;

import com.haulmont.testtask.Dao.AbstractDao;
import com.haulmont.testtask.Dao.AuthorDao;
import com.haulmont.testtask.Dao.BookDao;
import com.haulmont.testtask.Dao.GenreDao;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;

import java.io.IOException;
import java.sql.SQLException;

@Theme("mytheme")
public class HomePageView extends HorizontalLayout implements View {
    private Button initAuthor, initGenre, initBook;

    public HomePageView() {
        createUI();
    }

    private void createUI() {
        ThemeResource themeResource = new ThemeResource("img/descriptionHome.jpg");
        Image image = new Image("", themeResource);
        addComponents(image);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}
