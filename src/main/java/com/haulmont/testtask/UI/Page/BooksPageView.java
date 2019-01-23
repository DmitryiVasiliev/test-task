package com.haulmont.testtask.UI.Page;

import com.haulmont.testtask.Dao.DAOFactory;
import com.haulmont.testtask.Entity.Author;
import com.haulmont.testtask.Entity.Book;
import com.haulmont.testtask.UI.ModalWindow.AuthorModalWindow;
import com.haulmont.testtask.UI.ModalWindow.BookModalWindow;
import com.vaadin.annotations.Theme;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.filter.And;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;

import javax.xml.soap.Text;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Theme("mytheme")
public class BooksPageView extends VerticalLayout implements View {

    public Table table;
    private Button addButton, editButton, deleteButton, buttonFilter;
    private VerticalLayout verticalTable, verticalButton;
    private HorizontalLayout filterLayout;
    private String ADD_BUTTON = "Добавить";
    private String EDIT_BUTTON = "Изменить";
    private TextField textTitle, textAuthor, textPublisher;

    public BooksPageView() {
        createUI();
        createEvent();
    }

    private void createUI() {
        Label labelFilter = new Label("Фильтр: ");
        textTitle = new TextField();
        textAuthor = new TextField();
        textPublisher = new TextField();
        buttonFilter = new Button("Применить");
        textTitle.addStyleName("textfield");
        textAuthor.addStyleName("textfield");
        textPublisher.addStyleName("textfield");
        labelFilter.addStyleName("label");
        textTitle.setInputPrompt("Название");
        textAuthor.setInputPrompt("Автор");
        textPublisher.setInputPrompt("Издатель");
        filterLayout = new HorizontalLayout();
        filterLayout.addComponents(labelFilter, textTitle, textAuthor, textPublisher, buttonFilter);
        table = new Table();
        table.addStyleName("tableb");
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
        table.addContainerProperty("author", String.class, null, "Автор", null, Table.Align.CENTER);
        table.addContainerProperty("genre", String.class, null, "Жанр", null, Table.Align.CENTER);
        table.addContainerProperty("publisher", String.class, null, "Издатель", null, Table.Align.CENTER);
        table.addContainerProperty("year", String.class, null, "Год", null, Table.Align.CENTER);
        table.addContainerProperty("city", String.class, null, "Город", null, Table.Align.CENTER);
        table.setSelectable(true);
        table.setImmediate(true);
        table.setNullSelectionAllowed(false);
        table.setSizeFull();
        verticalTable.addStyleName("vertical-table");
        setWidth("100%");
        verticalTable.setWidth("70%");
        table.setWidth("100%");
        HorizontalLayout horizontalLayout2 = new HorizontalLayout();
        verticalTable.addComponent(table);
        verticalButton.addComponents(addButton, editButton, deleteButton);
        horizontalLayout2.addComponents(verticalTable, verticalButton);
        addComponents(filterLayout, horizontalLayout2);
    }

    private void createEvent() {
        table.addItemClickListener(event -> {
            editButton.setEnabled(true);
            deleteButton.setEnabled(true);
            Long id = (Long) event.getItem().getItemProperty("id").getValue();
            deleteButton.addClickListener(event1 -> {
                try {
                    DAOFactory.getDAOFactory(DAOFactory.HSQLDB).getBookDao().delete(id);
                    table.removeItem(event.getItemId());
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            editButton.addClickListener(event2 -> {
                BookModalWindow bookModalWindow = new BookModalWindow(table, EDIT_BUTTON);
                UI.getCurrent().addWindow(bookModalWindow);

            });
        });


        addButton.addClickListener(event -> {
            BookModalWindow bookModalWindow = new BookModalWindow(table, ADD_BUTTON);
            UI.getCurrent().addWindow(bookModalWindow);
        });

        buttonFilter.addClickListener(event -> {
            String title = textTitle.getValue().toLowerCase().trim();
            String author = textAuthor.getValue().toLowerCase().trim();
            String publisher = textPublisher.getValue().toLowerCase().trim();
            Container.Filterable filterable = (Container.Filterable) table.getContainerDataSource();
            filterable.removeAllContainerFilters();
            SimpleStringFilter stringFilterTitle = new SimpleStringFilter("title", title, true, false);
            SimpleStringFilter stringFilterAuthor = new SimpleStringFilter("author", author, true, false);
            SimpleStringFilter stringFilterPubl = new SimpleStringFilter("publisher", publisher, true, false);
            filterable.addContainerFilter(new And(stringFilterTitle, stringFilterAuthor, stringFilterPubl));


        });


    }

    private void setContent() {
        table.removeAllItems();
        List<Book> bookList = new ArrayList<>();
        try {
            bookList = DAOFactory.getDAOFactory(DAOFactory.HSQLDB).getBookDao().getAll();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Book book : bookList
        ) {
            Object newItemId = table.addItem();
            Item row1 = table.getItem(newItemId);
            row1.getItemProperty("id").setValue(book.getId());
            row1.getItemProperty("title").setValue(book.getTitle());
            row1.getItemProperty("author").setValue(book.getAuthor().toString());
            row1.getItemProperty("genre").setValue(book.getGenre().toString());
            row1.getItemProperty("publisher").setValue(book.getPublisher());
            row1.getItemProperty("year").setValue(book.getYear());
            row1.getItemProperty("city").setValue(book.getCity());

        }
        table.sort(new Object[]{"id"}, new boolean[]{true});
    }


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        setContent();

    }
}
