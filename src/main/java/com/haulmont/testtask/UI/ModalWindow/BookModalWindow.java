package com.haulmont.testtask.UI.ModalWindow;

import com.haulmont.testtask.Dao.DAOFactory;
import com.haulmont.testtask.Entity.Author;
import com.haulmont.testtask.Entity.Book;
import com.haulmont.testtask.Entity.Genre;
import com.haulmont.testtask.Entity.Publisher;
import com.haulmont.testtask.Validation.StringValidation;
import com.haulmont.testtask.Validation.YearValidation;
import com.vaadin.annotations.Theme;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.validator.*;
import com.vaadin.ui.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Theme("mytheme")
public class BookModalWindow extends Window {
    private Table table;
    private String TYPE_WINDOW;
    private Button addButton, cancelButton;
    private TextField textTitle, textYear, textCity;
    private ComboBox authorBox, genreBox, publisherBox;
    private Property propertyTitle;
    private Property propertyId;
    private Property propertyAuthor;
    private Property propertyGenre;
    private Property propertyPublisher;
    private Property propertyYear;
    private Property propertyCity;
    private List<Author> authorList;
    private List<Genre> genreList;
    private String[] publList = new String[]{"Питер", "Москва", "O’Reilly"};

    public BookModalWindow(Table table, String TYPE_WINDOW) {
        super(TYPE_WINDOW);
        this.table = table;
        this.TYPE_WINDOW = TYPE_WINDOW;
        textTitle = new TextField();
        authorBox = new ComboBox();
        genreBox = new ComboBox();
        publisherBox = new ComboBox();
        textYear = new TextField();
        textCity = new TextField();
        authorBox.addStyleName("combobox");
        genreBox.addStyleName("combobox");
        publisherBox.addStyleName("combobox");
        if (TYPE_WINDOW.equals("Изменить")) {
            propertyTitle = table.getContainerProperty(table.getValue(), "title");
            propertyAuthor = table.getContainerProperty(table.getValue(), "author");
            propertyGenre = table.getContainerProperty(table.getValue(), "genre");
            propertyPublisher = table.getContainerProperty(table.getValue(), "publisher");
            propertyYear = table.getContainerProperty(table.getValue(), "year");
            propertyCity = table.getContainerProperty(table.getValue(), "city");
            propertyId = table.getContainerProperty(table.getValue(), "id");
            textTitle.setValue((String) propertyTitle.getValue());
            // authorBox.setValue((String) propertyAuthor.getValue());
            authorBox.setValue(new String("Толстой Лев Николаевич"));
            genreBox.setValue((String) propertyGenre.getValue().toString());
            publisherBox.setValue((String) propertyGenre.getValue().toString());
            textYear.setValue((String) propertyYear.getValue());
            textCity.setValue((String) propertyCity.getValue());

        }
        createUI();
        createEvent();
    }

    private void createUI() {
        getAll();
        this.setClosable(false);
        this.setDraggable(false);
        GridLayout gridLayout = new GridLayout(2, 6);
        HorizontalLayout horizontalLayout3 = new HorizontalLayout();
        VerticalLayout verticalLayout = new VerticalLayout();
        addButton = new Button(TYPE_WINDOW);
        addButton.setEnabled(false);
        cancelButton = new Button("Отменить");
        authorBox.addItems(authorList);
        genreBox.addItems(genreList);
        publisherBox.addItems(publList);
        Label labelTitle = new Label("Название: ");
        Label labelAuthor = new Label("Автор: ");
        Label labelGenre = new Label("Жанр: ");
        Label labelPublisher = new Label("Издатель: ");
        Label labelYear = new Label("Год: ");
        Label labelCity = new Label("Город: ");
        gridLayout.addComponent(labelTitle, 0, 0);
        gridLayout.addComponent(textTitle, 1, 0);
        gridLayout.addComponent(labelAuthor, 0, 1);
        gridLayout.addComponent(authorBox, 1, 1);
        gridLayout.addComponent(labelGenre, 0, 2);
        gridLayout.addComponent(genreBox, 1, 2);
        gridLayout.addComponent(labelPublisher, 0, 3);
        gridLayout.addComponent(publisherBox, 1, 3);
        gridLayout.addComponent(labelYear, 0, 4);
        gridLayout.addComponent(textYear, 1, 4);
        gridLayout.addComponent(labelCity, 0, 5);
        gridLayout.addComponent(textCity, 1, 5);
        horizontalLayout3.addComponents(addButton, cancelButton);
        authorBox.addValidator(new NullValidator("Не может быть пустым", false));
        genreBox.addValidator(new NullValidator("Не может быть пустым", false));
        publisherBox.addValidator(new NullValidator("Не может быть пустым", false));
        textTitle.setInputPrompt("Введите название книги");
        textTitle.setRequired(true);
        textTitle.setImmediate(true);
        textTitle.setRequiredError("Введите название книги!");
        textTitle.addValidator(new StringLengthValidator("Слишком коротко", 1, 25, true));
        textYear.setRequired(true);
        textYear.setRequiredError("Введите год!");
        textYear.setImmediate(true);
        textYear.addValidator(new YearValidation("Неправильный формат"));
        textCity.setRequired(true);
        textCity.setRequiredError("Введите город");
        textCity.setImmediate(true);
        textCity.addValidator(new StringValidation("Только буквы"));
        textTitle.addStyleName("textfield");
        textCity.addStyleName("textfield");
        textYear.addStyleName("textfield");
        labelTitle.addStyleName("label");
        labelCity.addStyleName("label");
        labelYear.addStyleName("label");
        labelAuthor.addStyleName("label");
        labelGenre.addStyleName("label");
        labelPublisher.addStyleName("label");
        verticalLayout.addComponents(gridLayout, horizontalLayout3);
        center();
        setModal(true);
        setContent(verticalLayout);
    }

    private void getAll() {
        authorList = new ArrayList<>();
        genreList = new ArrayList<>();
        try {
            authorList = DAOFactory.getDAOFactory(DAOFactory.HSQLDB).getAuthorDao().getAll();
            genreList = DAOFactory.getDAOFactory(DAOFactory.HSQLDB).getGenreDao().getAll();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createEvent() {

        textTitle.addBlurListener(event -> {
            if (textTitle.isValid() && textCity.isValid() && textYear.isValid() && authorBox.isValid() && genreBox.isValid() && publisherBox.isValid())
                addButton.setEnabled(true);
        });
        textCity.addBlurListener(event -> {
            if (textTitle.isValid() && textCity.isValid() && textYear.isValid() && authorBox.isValid() && genreBox.isValid() && publisherBox.isValid())
                addButton.setEnabled(true);
        });
        textYear.addBlurListener(event -> {
            if (textTitle.isValid() && textCity.isValid() && textYear.isValid() && authorBox.isValid() && genreBox.isValid() && publisherBox.isValid())
                addButton.setEnabled(true);
        });

        authorBox.addBlurListener(event -> {
            if (textTitle.isValid() && textCity.isValid() && textYear.isValid() && authorBox.isValid() && genreBox.isValid() && publisherBox.isValid())
                addButton.setEnabled(true);
        });
        genreBox.addBlurListener(event -> {
            if (textTitle.isValid() && textCity.isValid() && textYear.isValid() && authorBox.isValid() && genreBox.isValid() && publisherBox.isValid())
                addButton.setEnabled(true);
        });
        publisherBox.addBlurListener(event -> {
            if (textTitle.isValid() && textCity.isValid() && textYear.isValid() && authorBox.isValid() && genreBox.isValid() && publisherBox.isValid())
                addButton.setEnabled(true);
        });


        cancelButton.addClickListener(event -> {
            close();
            UI.getCurrent().removeWindow(this);
        });

        addButton.addClickListener(event -> {

            switch (TYPE_WINDOW) {
                case "Добавить":
                    create();
                    break;
                case "Изменить":
                    edit();
                    break;
                default:
                    break;
            }

            close();
            UI.getCurrent().removeWindow(this);
        });
    }

    private void create() {
        long id = 0;
        Author author = (Author) authorBox.getValue();
        Genre genre = (Genre) genreBox.getValue();
        Book book = new Book(null, textTitle.getValue(), author, genre, publisherBox.getValue().toString(), textYear.getValue(), textCity.getValue());
        try {
            id = DAOFactory.getDAOFactory(DAOFactory.HSQLDB).getBookDao().create(book);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Object itemID = table.addItem();
        Item row = table.getItem(itemID);
        row.getItemProperty("id").setValue(id);
        row.getItemProperty("title").setValue(textTitle.getValue());
        row.getItemProperty("author").setValue(author.toString());
        row.getItemProperty("genre").setValue(genre.toString());
        row.getItemProperty("publisher").setValue(publisherBox.getValue().toString());
        row.getItemProperty("year").setValue(textYear.getValue());
        row.getItemProperty("city").setValue(textCity.getValue());
    }

    private void edit() {
        Author author = (Author) authorBox.getValue();
        Genre genre = (Genre) genreBox.getValue();
        Book book = new Book((Long) propertyId.getValue(), textTitle.getValue(), author, genre, publisherBox.getValue().toString(), textYear.getValue(), textCity.getValue());
        try {
            DAOFactory.getDAOFactory(DAOFactory.HSQLDB).getBookDao().update(book);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        propertyTitle.setValue(textTitle.getValue());
        propertyAuthor.setValue(author.toString());
        propertyGenre.setValue(genre.toString());
        propertyPublisher.setValue(publisherBox.getValue().toString());
        propertyYear.setValue(textYear.getValue());
        propertyCity.setValue(textCity.getValue());

    }


}

