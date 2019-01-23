package com.haulmont.testtask.UI.ModalWindow;

import com.haulmont.testtask.Dao.DAOFactory;
import com.haulmont.testtask.Entity.Author;
import com.haulmont.testtask.Entity.Genre;
import com.haulmont.testtask.Validation.StringValidation;
import com.haulmont.testtask.Validation.TitleValidation;
import com.vaadin.data.Item;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.ui.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

public class GenreModalWindow extends Window {
    private Table table;
    private String TYPE_WINDOW;
    private Button addButton, cancelButton;
    private TextField textTitle;

    public GenreModalWindow(Table table, String TYPE_WINDOW) {
        super(TYPE_WINDOW);
        this.table = table;
        this.TYPE_WINDOW = TYPE_WINDOW;
        textTitle = new TextField();
        if (TYPE_WINDOW.equals("Изменить"))
            textTitle.setValue((String) table.getContainerProperty(table.getValue(), "title").getValue());
        createUI();
        createEvent();
    }


    private void createUI() {
        this.setClosable(false);
        this.setDraggable(false);
        GridLayout gridLayout = new GridLayout(2, 3);
        HorizontalLayout horizontalLayout3 = new HorizontalLayout();
        VerticalLayout verticalLayout = new VerticalLayout();
        addButton = new Button(TYPE_WINDOW);
        addButton.setEnabled(false);
        cancelButton = new Button("Отменить");
        Label labelTitle = new Label("Название: ");
        labelTitle.addStyleName("label");
        textTitle.addStyleName("textfield");
        gridLayout.addComponent(labelTitle, 0, 0);
        gridLayout.addComponent(textTitle, 1, 0);
        horizontalLayout3.addComponents(addButton, cancelButton);
        textTitle.setInputPrompt("Введите жанр");
        textTitle.setRequiredError("Введите жанр!");
        textTitle.addValidator(new TitleValidation(table, "Такое название уже существует"));
        textTitle.addStyleName("label");
        verticalLayout.addComponents(gridLayout, horizontalLayout3);
        center();
        setModal(true);
        setContent(verticalLayout);
    }

    private void createEvent() {

        textTitle.addBlurListener(event -> {
            if (textTitle.isValid())
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
        Genre genre = new Genre(null, textTitle.getValue());
        long id = 0;
        try {
            id = DAOFactory.getDAOFactory(DAOFactory.HSQLDB).getGenreDao().create(genre);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Object itemID = table.addItem();
        Item row = table.getItem(itemID);
        row.getItemProperty("id").setValue(id);
        row.getItemProperty("title").setValue(textTitle.getValue());
        row.getItemProperty("stat").setValue(0);
    }

    private void edit() {
        Genre genre = new Genre((Long) table.getContainerProperty(table.getValue(), "id").getValue(), textTitle.getValue());
        try {
            DAOFactory.getDAOFactory(DAOFactory.HSQLDB).getGenreDao().update(genre);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        table.getContainerProperty(table.getValue(), "title").setValue(textTitle.getValue());
        table.getContainerProperty(table.getValue(), "stat").setValue(0);


    }
}
