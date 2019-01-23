package com.haulmont.testtask.UI.Page;

import com.haulmont.testtask.Dao.BookDao;
import com.haulmont.testtask.Dao.DAOFactory;
import com.haulmont.testtask.Entity.Author;
import com.haulmont.testtask.Entity.Genre;
import com.haulmont.testtask.UI.ModalWindow.AuthorModalWindow;
import com.haulmont.testtask.UI.ModalWindow.GenreModalWindow;
import com.vaadin.data.Item;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GenrePageView extends HorizontalLayout implements View {

    public Table table;
    private Button addButton, editButton, deleteButton;
    private VerticalLayout verticalTable, verticalButton;
    private String ADD_BUTTON = "Добавить";
    private String EDIT_BUTTON = "Изменить";

    public GenrePageView() {
        createUI();
        createEvent();
    }

    private void createUI() {
        table = new Table();
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
        table.addContainerProperty("title", String.class, null, "Название", null, Table.Align.CENTER);
        table.addContainerProperty("stat", Integer.class, null, "Статистика", null, Table.Align.CENTER);
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

        });
        deleteButton.addClickListener(event -> {
            try {
                Object rowId = table.getValue();
                Long id = (Long) table.getContainerProperty(rowId, "id").getValue();
                boolean flag = DAOFactory.getDAOFactory(DAOFactory.HSQLDB).getGenreDao().delete(id);
                if (flag)
                    table.removeItem(table.getValue());
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        editButton.addClickListener(event -> {
            GenreModalWindow genreModalWindow = new GenreModalWindow(table, EDIT_BUTTON);
            UI.getCurrent().addWindow(genreModalWindow);

        });


        addButton.addClickListener(event -> {
            GenreModalWindow genreModalWindow = new GenreModalWindow(table, ADD_BUTTON);
            UI.getCurrent().addWindow(genreModalWindow);
        });


    }

    private void setContent() {
        table.removeAllItems();
        BookDao bookDao = null;
        List<Genre> genreList = new ArrayList<>();
        try {
            genreList = DAOFactory.getDAOFactory(DAOFactory.HSQLDB).getGenreDao().getAll();
            bookDao = new BookDao();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Genre genre : genreList
        ) {
            Object newItemId = table.addItem();
            Item row1 = table.getItem(newItemId);
            row1.getItemProperty("id").setValue(genre.getId());
            row1.getItemProperty("title").setValue(genre.getTitle());
            try {
                row1.getItemProperty("stat").setValue(bookDao.statGenre(genre.getTitle()));
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (SQLException e2) {
                e2.printStackTrace();
            }

        }
        table.sort(new Object[]{"id"}, new boolean[]{true});

    }


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

        setContent();
    }
}