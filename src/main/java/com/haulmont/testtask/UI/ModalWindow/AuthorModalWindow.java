package com.haulmont.testtask.UI.ModalWindow;

import com.haulmont.testtask.Dao.AuthorDao;
import com.haulmont.testtask.Dao.DAOFactory;
import com.haulmont.testtask.Entity.Author;
import com.haulmont.testtask.Validation.StringValidation;
import com.vaadin.annotations.Theme;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.validator.CompositeValidator;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.ui.*;


import java.io.IOException;
import java.sql.SQLException;

@Theme("mytheme")
public class AuthorModalWindow extends Window {
    private Table table;
    private String TYPE_WINDOW;
    private Button addButton, cancelButton;
    private TextField textName, textSurname, textMiddlename;
    private Property propertyName;
    private Property propertyId;
    private Property propertySurname;
    private Property propertyMiddlename;

    public AuthorModalWindow(Table table, String TYPE_WINDOW) {
        super(TYPE_WINDOW);
        this.table = table;
        this.TYPE_WINDOW = TYPE_WINDOW;
        textName = new TextField();
        textSurname = new TextField();
        textMiddlename = new TextField();
        if (TYPE_WINDOW.equals("Изменить")) {
            propertyName = table.getContainerProperty(table.getValue(), "name");
            propertySurname = table.getContainerProperty(table.getValue(), "surname");
            propertyMiddlename = table.getContainerProperty(table.getValue(), "middlename");
            propertyId = table.getContainerProperty(table.getValue(), "id");
            textName.setValue((String) propertyName.getValue());
            textSurname.setValue((String) propertySurname.getValue());
            textMiddlename.setValue((String) propertyMiddlename.getValue());

        }
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
        Label labelName = new Label("Имя: ");
        Label labelSurname = new Label("Фамилия: ");
        Label labelMiddlename = new Label("Отчество: ");
        gridLayout.addComponent(labelName, 0, 0);
        gridLayout.addComponent(textName, 1, 0);
        gridLayout.addComponent(labelSurname, 0, 1);
        gridLayout.addComponent(textSurname, 1, 1);
        gridLayout.addComponent(labelMiddlename, 0, 2);
        gridLayout.addComponent(textMiddlename, 1, 2);
        horizontalLayout3.addComponents(addButton, cancelButton);
        textName.setInputPrompt("Введите имя автора");
        textSurname.setInputPrompt("Введите фамилию автора");
        textMiddlename.setInputPrompt("Введите отчество автора");
        textName.setRequired(true);
        textName.setImmediate(true);
        textName.setRequiredError("Введите имя автора!");
        textName.addValidator(new StringValidation("Только буквы и символы"));
        textName.setNullRepresentation("");
        textSurname.setRequired(true);
        textSurname.setRequiredError("Введите фамилию автора!");
        textSurname.addValidator(new StringValidation("Только буквы и символы"));
        textSurname.setNullRepresentation("");
        textMiddlename.setNullRepresentation("");
        textMiddlename.addStyleName("textfield");
        textName.addStyleName("textfield");
        textSurname.addStyleName("textfield");
        labelMiddlename.addStyleName("label");
        labelName.addStyleName("label");
        labelSurname.addStyleName("label");
        verticalLayout.addComponents(gridLayout, horizontalLayout3);
        center();
        setModal(true);
        setContent(verticalLayout);
    }

    private void createEvent() {

        textName.addBlurListener(event -> {
            if (textName.isValid() && textSurname.isValid() && textMiddlename.isValid())
                addButton.setEnabled(true);
        });
        textSurname.addBlurListener(event -> {
            if (textName.isValid() && textSurname.isValid() && textMiddlename.isValid())
                addButton.setEnabled(true);
        });
        textMiddlename.addBlurListener(event -> {
            if (textName.isValid() && textSurname.isValid() && textMiddlename.isValid())
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
        Author author = new Author(null, textName.getValue(), textSurname.getValue(), textMiddlename.getValue());
        try {
            id = DAOFactory.getDAOFactory(DAOFactory.HSQLDB).getAuthorDao().create(author);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Object itemID = table.addItem();
        Item row = table.getItem(itemID);
        row.getItemProperty("id").setValue(id);
        row.getItemProperty("name").setValue(textName.getValue());
        row.getItemProperty("surname").setValue(textSurname.getValue());
        row.getItemProperty("middlename").setValue(textMiddlename.getValue());
    }

    private void edit() {
        Author author = new Author((Long) propertyId.getValue(), textName.getValue(), textSurname.getValue(), textMiddlename.getValue());
        try {
            DAOFactory.getDAOFactory(DAOFactory.HSQLDB).getAuthorDao().update(author);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        propertyName.setValue(textName.getValue());
        propertySurname.setValue(textSurname.getValue());
        propertyMiddlename.setValue(textMiddlename.getValue());

    }


}
