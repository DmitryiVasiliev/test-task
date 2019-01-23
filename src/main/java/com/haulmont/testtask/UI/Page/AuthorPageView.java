package com.haulmont.testtask.UI.Page;

import com.haulmont.testtask.Dao.AbstractDao;
import com.haulmont.testtask.Dao.AuthorDao;
import com.haulmont.testtask.Dao.DAOFactory;
import com.haulmont.testtask.Entity.Author;
import com.haulmont.testtask.UI.ModalWindow.AuthorModalWindow;
import com.vaadin.annotations.Theme;
import com.vaadin.data.Item;
import com.vaadin.event.MouseEvents;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Theme("mytheme")
public class AuthorPageView extends HorizontalLayout implements View {

    public Table table;
    private Button addButton, editButton, deleteButton;
    private VerticalLayout verticalTable, verticalButton;
    private HorizontalLayout horizontalLayout;
    private String ADD_BUTTON = "Добавить";
    private String EDIT_BUTTON = "Изменить";

    public AuthorPageView() {
        createUI();
        createEvent();
    }

    private void createUI() {
        table = new Table();
        horizontalLayout = new HorizontalLayout();
        verticalButton = new VerticalLayout();
        verticalTable = new VerticalLayout();
        addButton = new Button("Добавить");
        editButton = new Button("Изменить");
        deleteButton = new Button("Удалить");
        editButton.setEnabled(false);
        deleteButton.setEnabled(false);
        addButton.addStyleName("buttoncontrl");
        editButton.addStyleName("buttoncontrl2");
        deleteButton.addStyleName("buttoncontrl2");
        table.addContainerProperty("id", Long.class, null, "ID", null, Table.Align.CENTER);
        table.addContainerProperty("name", String.class, null, "Имя", null, Table.Align.CENTER);
        table.addContainerProperty("surname", String.class, null, "Фамилия", null, Table.Align.CENTER);
        table.addContainerProperty("middlename", String.class, null, "Отчество", null, Table.Align.CENTER);
        table.setSelectable(true);
        table.setImmediate(true);
        table.setNullSelectionAllowed(false);
        table.setSizeFull();
        verticalTable.addStyleName("vertical-table");
        setWidth("100%");
        verticalTable.setWidth("70%");
        table.setWidth("100%");
        verticalTable.addComponent(table);
        verticalButton.addComponents(addButton, editButton, deleteButton);
        addComponents(verticalTable, verticalButton);
    }

    private void createEvent() {
        table.addItemClickListener(event -> {
            editButton.setEnabled(true);
            deleteButton.setEnabled(true);
            Long id = (Long) event.getItem().getItemProperty("id").getValue();
            deleteButton.addClickListener(event1 -> {
                try {
                    boolean flag = DAOFactory.getDAOFactory(DAOFactory.HSQLDB).getAuthorDao().delete(id);
                    if (flag)
                        table.removeItem(event.getItemId());
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            editButton.addClickListener(event2 -> {
                AuthorModalWindow authorModalWindow = new AuthorModalWindow(table, EDIT_BUTTON);
                UI.getCurrent().addWindow(authorModalWindow);

            });
        });


        addButton.addClickListener(event -> {
            AuthorModalWindow authorModalWindow = new AuthorModalWindow(table, ADD_BUTTON);
            UI.getCurrent().addWindow(authorModalWindow);
        });


    }

    private void setContent() {
        table.removeAllItems();
        List<Author> authorList = new ArrayList<>();
        try {
            authorList = DAOFactory.getDAOFactory(DAOFactory.HSQLDB).getAuthorDao().getAll();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Author author : authorList
        ) {
            Object newItemId = table.addItem();
            Item row1 = table.getItem(newItemId);
            row1.getItemProperty("id").setValue(author.getId());
            row1.getItemProperty("name").setValue(author.getName());
            row1.getItemProperty("surname").setValue(author.getSurname());
            row1.getItemProperty("middlename").setValue(author.getMiddlename());

        }
        table.sort(new Object[]{"id"}, new boolean[]{true});
    }


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

        setContent();
    }
}
