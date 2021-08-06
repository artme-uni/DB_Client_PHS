package phs;

import model.Role;
import model.database.DatabaseModel;
import model.properties.PropertiesBundle;
import model.properties.PropertyInfo;
import model.properties.PropertyType;

public class DatabasePHS {
    private final DatabaseModel databaseModel = new DatabaseModel();

    public DatabasePHS() {
        createClients();
        createOutletTypes();
        createOutlets();
        createProfessions();
        createJobs();
        createFirms();
        createItems();
        createStorage();
        createVendors();
        createVendorItems();
        createDeliveries();
        createReceipts();
        createOrders();
        createServiceTypes();
        createServiceOrders();
        createPaperSizes();
        createPaperTypes();
        createPrintPrices();
        createPrintDiscounts();
        createPrintOrders();
        createFrames();
        createDeveloperPrices();
        createDeveloperOrders();
        createSaleOrders();
    }

    private void createClients() {
        String tableName = "Clients";

        PropertiesBundle table = databaseModel.addTable(tableName, "Клиенты");
        table.addProperty("ID", "ID клиент", PropertyType.ID);
        table.addProperty("Second_name", "Фамилия", PropertyType.STRING);
        table.addProperty("First_name", "Имя", PropertyType.STRING);
        table.addProperty("Middle_name", "Отчество", PropertyType.STRING);
        table.addProperty("Discount", "Скидка", PropertyType.INTEGER);
        table.addProperty("Is_Professional", "Профессионал", PropertyType.BOOLEAN);

        table.addRepresentationProperty("Second_name");
        table.addRepresentationProperty("First_name");
        table.addRepresentationProperty("Middle_name");

        databaseModel.grantWriteAccess(Role.ADMINISTRATOR.toString(), tableName);
        databaseModel.grantWriteAccess(Role.ACCOUNTANT.toString(), tableName);
    }

    private void createOutletTypes() {
        String tableName = "Outlet_types";

        PropertiesBundle table = databaseModel.addTable(tableName, "Типы торговых точек");
        table.addProperty("ID", "ID типа точки", PropertyType.ID);
        table.addProperty("Name", "Название типа", PropertyType.STRING);

        databaseModel.grantWriteAccess(Role.ADMINISTRATOR.toString(), tableName);

        table.addRepresentationProperty("Name");

        databaseModel.grantReadOnlyAccess(Role.HR_MANAGER.toString(), tableName);
        databaseModel.grantReadOnlyAccess(Role.BUYER.toString(), tableName);
    }

    private void createOutlets() {
        String tableName = "Outlets";

        PropertiesBundle table = databaseModel.addTable(tableName, "Торговые точки");
        table.addProperty("ID", "ID точки", PropertyType.ID);
        table.addReferenceProperty("Type_ID", "Тип точки", "Outlet_types");
        table.addReferenceProperty("Related_outlet_ID", "Филиал", "Outlets");
        table.addProperty("Address", "Адрес", PropertyType.STRING);

        table.addRepresentationProperty("Type_ID");
        table.addRepresentationProperty("Address");

        databaseModel.grantWriteAccess(Role.ADMINISTRATOR.toString(), tableName);

        databaseModel.grantReadOnlyAccess(Role.HR_MANAGER.toString(), tableName);
        databaseModel.grantReadOnlyAccess(Role.BUYER.toString(), tableName);
    }

    private void createProfessions() {
        String tableName = "Professions";

        PropertiesBundle table = databaseModel.addTable(tableName, "Профессии");
        table.addProperty("ID", "ID профессии", PropertyType.ID);
        table.addProperty("Profession", "Название профессии", PropertyType.STRING);

        table.addRepresentationProperty("Profession");

        databaseModel.grantWriteAccess(Role.ADMINISTRATOR.toString(), tableName);
        databaseModel.grantWriteAccess(Role.HR_MANAGER.toString(), tableName);
    }

    private void createJobs() {
        String tableName = "Jobs";

        PropertiesBundle table = databaseModel.addTable(tableName, "Рабочие места");
        table.addProperty("ID", "ID рабочего места", PropertyType.ID);
        table.addReferenceProperty("Outlet_ID", "Торговая точка", "Outlets");
        table.addReferenceProperty("Profession_ID", "Профессия", "Professions");
        table.addProperty("Amount", "Количество", PropertyType.INTEGER);

        table.addRepresentationProperty("Outlet_ID");
        table.addRepresentationProperty("Profession_ID");

        databaseModel.grantWriteAccess(Role.ADMINISTRATOR.toString(), tableName);
        databaseModel.grantWriteAccess(Role.HR_MANAGER.toString(), tableName);
    }

    private void createFirms() {
        String tableName = "Firms";

        PropertiesBundle table = databaseModel.addTable(tableName, "Фирмы товаров");
        table.addProperty("ID", "ID фирмы", PropertyType.ID);
        table.addProperty("Name", "Название фирмы", PropertyType.STRING);

        table.addRepresentationProperty("Name");

        databaseModel.grantWriteAccess(Role.ADMINISTRATOR.toString(), tableName);
        databaseModel.grantWriteAccess(Role.BUYER.toString(), tableName);
    }

    private void createItems() {
        String tableName = "Items";

        PropertiesBundle table = databaseModel.addTable(tableName, "Вещи");
        table.addProperty("ID", "ID вещи", PropertyType.ID);
        table.addReferenceProperty("Firm_ID", "Фирма", "Firms");
        table.addProperty("Product_name", "Название", PropertyType.STRING);
        table.addProperty("Price", "Цена", PropertyType.DOUBLE);

        table.addRepresentationProperty("Product_name");

        databaseModel.grantWriteAccess(Role.ADMINISTRATOR.toString(), tableName);
        databaseModel.grantWriteAccess(Role.BUYER.toString(), tableName);
    }

    private void createStorage() {
        String tableName = "Storage";

        PropertiesBundle table = databaseModel.addTable(tableName, "Остатки на складах");
        table.addProperty("ID", "ID остатка", PropertyType.ID);
        table.addReferenceProperty("Outlet_ID", "Торговая точка", "Outlets");
        table.addReferenceProperty("Item_ID", "Товар", "Items");
        table.addProperty("Balance", "Количество", PropertyType.INTEGER);

        databaseModel.grantWriteAccess(Role.ADMINISTRATOR.toString(), tableName);
        databaseModel.grantWriteAccess(Role.BUYER.toString(), tableName);
    }

    private void createVendors() {
        String tableName = "Vendors";

        PropertiesBundle table = databaseModel.addTable(tableName, "Продавцы");
        table.addProperty("ID", "ID продавца", PropertyType.ID);
        table.addProperty("Name", "Название продавца", PropertyType.STRING);

        table.addRepresentationProperty("Name");

        databaseModel.grantWriteAccess(Role.ADMINISTRATOR.toString(), tableName);
        databaseModel.grantWriteAccess(Role.BUYER.toString(), tableName);
    }

    private void createVendorItems() {
        String tableName = "Vendor_Items";

        PropertiesBundle table = databaseModel.addTable(tableName, "Товары продавцов");
        table.addReferenceProperty("Vendor_ID", "Продавец", "Vendors");
        table.addReferenceProperty("Item_ID", "Товар", "Items");

        databaseModel.grantWriteAccess(Role.ADMINISTRATOR.toString(), tableName);
        databaseModel.grantWriteAccess(Role.BUYER.toString(), tableName);
    }

    private void createDeliveries() {
        String tableName = "Deliveries";

        PropertiesBundle table = databaseModel.addTable(tableName, "Поставки");
        table.addProperty("ID", "ID поставки", PropertyType.ID);
        table.addReferenceProperty("Outlet_ID", "Торговая точка", "Outlets");
        table.addReferenceProperty("Item_ID", "Товар", "Items");
        table.addReferenceProperty("Vendor_ID", "Продавец", "Vendors");
        table.addProperty("Amount", "Количество", PropertyType.INTEGER);
        table.addProperty("Delivery_Date", "Дата поставки", PropertyType.DATE);
        table.addProperty("Purchase_price", "Стоимость", PropertyType.DOUBLE);

        databaseModel.grantWriteAccess(Role.ADMINISTRATOR.toString(), tableName);
        databaseModel.grantWriteAccess(Role.BUYER.toString(), tableName);
    }

    private void createReceipts() {
        String tableName = "Receipts";

        PropertiesBundle table = databaseModel.addTable(tableName, "Чеки");
        table.addProperty("ID", "ID чека", PropertyType.ID);
        table.addReferenceProperty("Client_ID", "Клиент", "Clients");
        table.addReferenceProperty("Outlet_ID", "Торговая точка", "Outlets");
        table.addProperty("Creation_date", "Дата заказа", PropertyType.DATE);
        table.addProperty("Total", "Сумма", PropertyType.DOUBLE);

        table.addRepresentationProperty("ID");
        table.addRepresentationProperty("Outlet_ID");

        databaseModel.grantWriteAccess(Role.ADMINISTRATOR.toString(), tableName);
        databaseModel.grantWriteAccess(Role.ACCOUNTANT.toString(), tableName);
    }

    private void createOrders() {
        String tableName = "Orders";

        PropertiesBundle table = databaseModel.addTable(tableName, "Заказы");
        table.addProperty("ID", "ID заказа", PropertyType.ID);
        table.addReferenceProperty("Receipt_ID", "Чек", "Receipts");
        table.addProperty("Is_urgent", "Срочный", PropertyType.BOOLEAN);

        table.addRepresentationProperty("ID");
        table.addRepresentationProperty("Receipt_ID");

        databaseModel.grantWriteAccess(Role.ADMINISTRATOR.toString(), tableName);
        databaseModel.grantWriteAccess(Role.ACCOUNTANT.toString(), tableName);
    }

    private void createServiceTypes() {
        String tableName = "Service_types";

        PropertiesBundle table = databaseModel.addTable(tableName, "Типы услуг");
        table.addProperty("ID", "ID услуги", PropertyType.ID);
        table.addProperty("Name", "Название услуги", PropertyType.STRING);
        table.addProperty("Price", "Стоимость", PropertyType.DOUBLE);

        table.addRepresentationProperty("Name");

        databaseModel.grantWriteAccess(Role.ADMINISTRATOR.toString(), tableName);
        databaseModel.grantWriteAccess(Role.BUYER.toString(), tableName);

        databaseModel.grantReadOnlyAccess(Role.ACCOUNTANT.toString(), tableName);

    }

    private void createServiceOrders() {
        String tableName = "Service_orders";

        PropertiesBundle table = databaseModel.addTable(tableName, "Заказы услуг");
        table.addReferenceProperty("Order_ID", "Заказ", "Orders");
        table.addReferenceProperty("Service_type_ID", "Тип услуги", "Service_types");

        table.addRepresentationProperty("Order_ID");
        table.addRepresentationProperty("Service_type_ID");

        databaseModel.grantWriteAccess(Role.ADMINISTRATOR.toString(), tableName);
        databaseModel.grantWriteAccess(Role.ACCOUNTANT.toString(), tableName);
    }

    private void createPaperSizes() {
        String tableName = "Paper_sizes";

        PropertiesBundle table = databaseModel.addTable(tableName, "Размеры бумаги");
        table.addProperty("ID", "ID размера", PropertyType.ID);
        table.addProperty("Name", "Название размера", PropertyType.STRING);

        table.addRepresentationProperty("Name");

        databaseModel.grantWriteAccess(Role.ADMINISTRATOR.toString(), tableName);
        databaseModel.grantWriteAccess(Role.BUYER.toString(), tableName);

        databaseModel.grantReadOnlyAccess(Role.ACCOUNTANT.toString(), tableName);
    }

    private void createPaperTypes() {
        String tableName = "Paper_types";

        PropertiesBundle table = databaseModel.addTable(tableName, "Типы бумаги");
        table.addProperty("ID", "ID типа", PropertyType.ID);
        table.addProperty("Name", "Название типа", PropertyType.STRING);

        table.addRepresentationProperty("Name");

        databaseModel.grantWriteAccess(Role.ADMINISTRATOR.toString(), tableName);
        databaseModel.grantWriteAccess(Role.BUYER.toString(), tableName);

        databaseModel.grantReadOnlyAccess(Role.ACCOUNTANT.toString(), tableName);
    }

    private void createPrintPrices() {
        String tableName = "Print_prices";

        PropertiesBundle table = databaseModel.addTable(tableName, "Цены на печать");
        table.addProperty("ID", "ID цены", PropertyType.ID);
        table.addReferenceProperty("Paper_size_ID", "Размер бумаги", "Paper_sizes");
        table.addReferenceProperty("Paper_type_ID", "Тип бумаги", "Paper_types");
        table.addProperty("Price", "Цена", PropertyType.DOUBLE);

        databaseModel.grantWriteAccess(Role.ADMINISTRATOR.toString(), tableName);
        databaseModel.grantWriteAccess(Role.BUYER.toString(), tableName);
    }

    private void createPrintDiscounts() {
        String tableName = "Print_discounts";

        PropertiesBundle table = databaseModel.addTable(tableName, "Скидки на печать");
        table.addProperty("ID", "ID скидки", PropertyType.ID);
        table.addProperty("Photo_amount", "Количество фотографий", PropertyType.INTEGER);
        table.addProperty("Discount", "Скидка", PropertyType.INTEGER);

        table.addRepresentationProperty("Photo_amount");

        databaseModel.grantWriteAccess(Role.ADMINISTRATOR.toString(), tableName);
        databaseModel.grantWriteAccess(Role.ACCOUNTANT.toString(), tableName);
    }

    private void createPrintOrders() {
        String tableName = "Print_orders";

        PropertiesBundle table = databaseModel.addTable(tableName, "Заказы на печать");
        table.addReferenceProperty("Order_ID", "Заказ", "Orders");
        table.addProperty("Discount", "Скидка", PropertyType.INTEGER);

        table.addRepresentationProperty("Order_ID");
        table.addRepresentationProperty("Discount");

        databaseModel.grantWriteAccess(Role.ADMINISTRATOR.toString(), tableName);
        databaseModel.grantWriteAccess(Role.ACCOUNTANT.toString(), tableName);
    }

    private void createFrames() {
        String tableName = "Frames";

        PropertiesBundle table = databaseModel.addTable(tableName, "Кадры для печати");
        table.addProperty("ID", "ID кадра", PropertyType.ID);
        table.addReferenceProperty("Print_order_ID", "Заказ", "Orders");
        table.addReferenceProperty("Paper_size_ID", "Размер бумаги", "Paper_sizes");
        table.addReferenceProperty("Paper_type_ID", "Тип бумаги", "Paper_types");
        table.addProperty("Frame_code", "Идентификатор кадра", PropertyType.STRING);
        table.addProperty("Amount", "Количество", PropertyType.INTEGER);

        table.addRepresentationProperty("Print_order_ID");
        table.addRepresentationProperty("Frame_code");

        databaseModel.grantWriteAccess(Role.ADMINISTRATOR.toString(), tableName);
        databaseModel.grantReadOnlyAccess(Role.ACCOUNTANT.toString(), tableName);
    }

    private void createDeveloperPrices() {
        String tableName = "Developer_prices";

        PropertiesBundle table = databaseModel.addTable(tableName, "Цены на проявку");
        table.addProperty("ID", "ID цены", PropertyType.ID);
        table.addProperty("Price_name", "Название цены", PropertyType.STRING);
        table.addProperty("Price", "Цена", PropertyType.DOUBLE);

        table.addRepresentationProperty("Price_name");

        databaseModel.grantWriteAccess(Role.ADMINISTRATOR.toString(), tableName);
        databaseModel.grantWriteAccess(Role.ACCOUNTANT.toString(), tableName);
    }

    private void createDeveloperOrders() {
        String tableName = "Developer_orders";

        PropertiesBundle table = databaseModel.addTable(tableName, "Заказы на проявку");
        table.addReferenceProperty("Order_ID", "Заказ", "Orders");
        table.addReferenceProperty("Film_receipt_ID", "Чек для пленки", "Receipts");

        databaseModel.grantWriteAccess(Role.ADMINISTRATOR.toString(), tableName);
        databaseModel.grantWriteAccess(Role.ACCOUNTANT.toString(), tableName);
    }

    private void createSaleOrders() {
        String tableName = "Sale_orders";

        PropertiesBundle table = databaseModel.addTable(tableName, "Заказы фототоваров");
        table.addReferenceProperty("Order_ID", "Заказ", "Orders");
        table.addReferenceProperty("Product_ID", "Товар", "Items");
        table.addProperty("Amount", "Количество", PropertyType.INTEGER);

        databaseModel.grantWriteAccess(Role.ADMINISTRATOR.toString(), tableName);
        databaseModel.grantWriteAccess(Role.ACCOUNTANT.toString(), tableName);
    }

    public DatabaseModel getModel(){
        return databaseModel;
    }


}
