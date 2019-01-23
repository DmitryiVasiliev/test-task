package com.haulmont.testtask.UI.Page;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;

@Theme("mytheme")
public class MainUI extends UI {

    @Override
    protected void init(VaadinRequest request) {
        getPage().setTitle("Информация о книгах библиотеки");
        Button homePageB = new Button("Главная");
        Button authorPageB = new Button("Авторы");
        Button genrePageB = new Button("Жанры");
        Button booksPageB = new Button("Книги");
        HorizontalLayout header = new HorizontalLayout();
        header.addStyleName("header-layout");
        header.setWidth("98%");
        header.setMargin(false);
        header.setSpacing(true);
        homePageB.addStyleName("button");
        authorPageB.addStyleName("button");
        genrePageB.addStyleName("button");
        booksPageB.addStyleName("button");
        header.addComponents(homePageB, authorPageB, genrePageB, booksPageB);

        VerticalLayout showLayout = new VerticalLayout();
        showLayout.setWidth("98%");
        showLayout.setHeight("90%");
        showLayout.addComponent(new Label("123"));
        showLayout.addStyleName("view-layout");
        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.addStyleName("main-layout");
        mainLayout.setHeight("100%");
        mainLayout.addComponents(header, showLayout);
        mainLayout.setExpandRatio(header, 1);
        mainLayout.setExpandRatio(showLayout, 9);

        ViewDisplay viewDisplay = new Navigator.ComponentContainerViewDisplay(showLayout);
        Navigator navigator = new Navigator(this, viewDisplay);
        navigator.addView(NamePage.HomePage, new HomePageView());
        navigator.addView(NamePage.AuthorPage, new AuthorPageView());
        navigator.addView(NamePage.GenrePage, new GenrePageView());
        navigator.addView(NamePage.BooksPage, new BooksPageView());

        homePageB.addClickListener(clickEvent -> navigator.navigateTo(NamePage.HomePage));
        authorPageB.addClickListener(clickEvent -> navigator.navigateTo(NamePage.AuthorPage));
        genrePageB.addClickListener(clickEvent -> navigator.navigateTo(NamePage.GenrePage));
        booksPageB.addClickListener(clickEvent -> navigator.navigateTo(NamePage.BooksPage));

        setContent(mainLayout);

    }
}