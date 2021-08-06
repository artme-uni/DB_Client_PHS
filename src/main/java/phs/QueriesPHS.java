package phs;

import model.Role;
import model.properties.PropertyType;
import model.properties.PropertiesBundle;
import model.queries.QueriesModel;

public class QueriesPHS {
    private final QueriesModel queriesModel = new QueriesModel();

    private static final String[] OUTLET_TYPES = new String[]{"Киоск", "Фотомагазин", "Филиал"};
    private static final String[] ORDERS_TYPES = new String[]{"Продажа фототовара", "Печать фотографий", "Оказание фотоуслуг", "Проявка фотопленки"};

    public QueriesPHS() {
        createQuery1();
        createQuery2();
        createQuery3();
        createQuery4();
        createQuery5();
        createQuery6();
        createQuery7();
        createQuery8();
        createQuery9();
        createQuery10();
        createQuery11();
        createQuery12();
    }

    private void createQuery1(){
        String queryName = "1. Перечень пунктов приема заказов";

        PropertiesBundle parametersInfo = queriesModel.addQuery(queryName);
        PropertiesBundle tableInfo = queriesModel.getTable(queryName);

//        tableInfo.addProperty("OUTLET_ID", "ID точки", PropertyType.ID);
        tableInfo.addProperty("TYPE", "Тип точки", PropertyType.STRING);
        tableInfo.addProperty("ADDRESS", "Адрес", PropertyType.STRING);
        tableInfo.addReferenceProperty("RELATED_OUTLET_ID", "Филиал", "Outlets");

        parametersInfo.addProperty("outlet_types", "Тип точки", PropertyType.DROPDOWN);
        parametersInfo.addReferenceProperty("related_outlet_id", "Филиал", "Outlets");

        parametersInfo.addDropdownValues("outlet_types", OUTLET_TYPES);

        queriesModel.grantAccess(Role.ADMINISTRATOR.toString(), queryName);
        queriesModel.grantAccess(Role.HR_MANAGER.toString(), queryName);
    }

    private void createQuery2(){
        String queryName = "2. Перечень всех заказов";

        PropertiesBundle parametersInfo = queriesModel.addQuery(queryName);
        PropertiesBundle tableInfo = queriesModel.getTable(queryName);

//        tableInfo.addProperty("ORDER_ID", "ID Заказа", PropertyType.ID);
        tableInfo.addProperty("ORDER_TYPE", "Тип заказа", PropertyType.STRING);
        tableInfo.addReferenceProperty("RELATED_OUTLET_ID", "Филиал", "Outlets");
        tableInfo.addProperty("ADDRESS", "Адрес", PropertyType.STRING);
        tableInfo.addProperty("OUTLET_TYPE", "Тип точки", PropertyType.STRING);
        tableInfo.addProperty("ORDER_DATE", "Дата заказа", PropertyType.DATE);

        parametersInfo.addProperty("start_date", "Дата заказа (с)", PropertyType.DATE);
        parametersInfo.addProperty("end_date", "Дата заказа (по)", PropertyType.DATE);
        parametersInfo.addProperty("outlet_types", "Тип точки", PropertyType.DROPDOWN);
        parametersInfo.addReferenceProperty("related_outlet_id", "Филиал", "Outlets");

        parametersInfo.addDropdownValues("outlet_types", OUTLET_TYPES);

        queriesModel.grantAccess(Role.ADMINISTRATOR.toString(), queryName);
        queriesModel.grantAccess(Role.ACCOUNTANT.toString(), queryName);
    }

    private void createQuery3(){
        String queryName = "3. Перечень заказов на отдельные виды фоторабот";

        PropertiesBundle parametersInfo = queriesModel.addQuery(queryName);
        PropertiesBundle tableInfo = queriesModel.getTable(queryName);

//        tableInfo.addProperty("ORDER_ID", "ID заказа", PropertyType.ID);
        tableInfo.addProperty("ORDER_TYPE", "Тип заказа", PropertyType.STRING);
        tableInfo.addProperty("IS_URGENT", "Срочный", PropertyType.BOOLEAN);
        tableInfo.addReferenceProperty("RELATED_OUTLET_ID", "Филиал", "Outlets");
        tableInfo.addProperty("ADDRESS", "Адрес", PropertyType.STRING);
        tableInfo.addProperty("ORDER_DATE", "Дата ", PropertyType.DATE);

        parametersInfo.addProperty("start_date", "Дата заказа (с)", PropertyType.DATE);
        parametersInfo.addProperty("end_date", "Дата заказа (по)", PropertyType.DATE);
        parametersInfo.addReferenceProperty("related_outlet_id", "Филиал", "Outlets");
        parametersInfo.addProperty("is_urgen", "Срочный", PropertyType.BOOLEAN);
        parametersInfo.addProperty("order_types", "Тип заказа", PropertyType.DROPDOWN);

        parametersInfo.addDropdownValues("order_types", ORDERS_TYPES);

        queriesModel.grantAccess(Role.ADMINISTRATOR.toString(), queryName);
        queriesModel.grantAccess(Role.ACCOUNTANT.toString(), queryName);
    }

    private void createQuery4(){
        String queryName = "4. Сумма выручки с заказов";

        PropertiesBundle parametersInfo = queriesModel.addQuery(queryName);
        PropertiesBundle tableInfo = queriesModel.getTable(queryName);

        tableInfo.addProperty("TOTAL", "Сумма", PropertyType.DOUBLE);

        parametersInfo.addProperty("start_date", "Дата заказа (с)", PropertyType.DATE);
        parametersInfo.addProperty("end_date", "Дата заказа (по)", PropertyType.DATE);
        parametersInfo.addProperty("is_urgent", "Срочный", PropertyType.BOOLEAN);
        parametersInfo.addReferenceProperty("outlet_id", "Торговая точка", "Outlets");
        parametersInfo.addProperty("outlet_types", "Тип точки", PropertyType.DROPDOWN);
        parametersInfo.addProperty("order_types", "Тип заказа", PropertyType.DROPDOWN);

        parametersInfo.addDropdownValues("outlet_types", OUTLET_TYPES);
        parametersInfo.addDropdownValues("order_types", ORDERS_TYPES);

        queriesModel.grantAccess(Role.ADMINISTRATOR.toString(), queryName);
        queriesModel.grantAccess(Role.ACCOUNTANT.toString(), queryName);
    }

    private void createQuery5(){
        String queryName = "5. Количество отпечатанных фотографий";

        PropertiesBundle parametersInfo = queriesModel.addQuery(queryName);
        PropertiesBundle tableInfo = queriesModel.getTable(queryName);

        tableInfo.addProperty("FRAME_COUNT", "Количество фотографий", PropertyType.INTEGER);

        parametersInfo.addProperty("start_date", "Дата заказа (с)", PropertyType.DATE);
        parametersInfo.addProperty("end_date", "Дата заказа (по)", PropertyType.DATE);
        parametersInfo.addReferenceProperty("outlet_id", "Торговая точка", "Outlets");
        parametersInfo.addReferenceProperty("related_outlet_id", "Филиал", "Outlets");
        parametersInfo.addProperty("is_urgent", "Срочные заказы", PropertyType.BOOLEAN);

        queriesModel.grantAccess(Role.ADMINISTRATOR.toString(), queryName);
        queriesModel.grantAccess(Role.BUYER.toString(), queryName);
    }

    private void createQuery6(){
        String queryName = "6. Количество проявленных фотопленок";

        PropertiesBundle parametersInfo = queriesModel.addQuery(queryName);
        PropertiesBundle tableInfo = queriesModel.getTable(queryName);

        tableInfo.addProperty("FILM_COUNT", "Количество фотопленок", PropertyType.INTEGER);

        parametersInfo.addProperty("start_date", "Дата заказа (с)", PropertyType.DATE);
        parametersInfo.addProperty("end_date", "Дата заказа (по)", PropertyType.DATE);
        parametersInfo.addReferenceProperty("outlet_id", "Торговая точка", "Outlets");
        parametersInfo.addReferenceProperty("related_outlet_id", "Филиал", "Outlets");
        parametersInfo.addProperty("is_urgent", "Срочные заказы", PropertyType.BOOLEAN);

        queriesModel.grantAccess(Role.ADMINISTRATOR.toString(), queryName);
        queriesModel.grantAccess(Role.BUYER.toString(), queryName);
    }

    private void createQuery7(){
        String queryName = "7. Перечень поставщиков";

        PropertiesBundle parametersInfo = queriesModel.addQuery(queryName);
        PropertiesBundle tableInfo = queriesModel.getTable(queryName);

//        tableInfo.addReferenceProperty("VENDOR_ID", "ID поставщика", PropertyType.REFERENCE);
        tableInfo.addProperty("VENDOR_NAME", "Название поставщика", PropertyType.STRING);
        tableInfo.addProperty("DELIVERIES_COUNT", "Количество поставок", PropertyType.INTEGER);
        tableInfo.addProperty("ITEMS_COUNT", "Количество позиций товаров", PropertyType.INTEGER);

        parametersInfo.addProperty("start_date", "Дата заказа (с)", PropertyType.DATE);
        parametersInfo.addProperty("end_date", "Дата заказа (по)", PropertyType.DATE);
        parametersInfo.addProperty("min_deliveries_count", "Мин. кол-во поставок", PropertyType.INTEGER);
        parametersInfo.addProperty("min_items_count", "Мин. кол-во позиций", PropertyType.INTEGER);

        queriesModel.grantAccess(Role.ADMINISTRATOR.toString(), queryName);
        queriesModel.grantAccess(Role.BUYER.toString(), queryName);
    }

    private void createQuery8(){
        String queryName = "8. Список клиентов";

        PropertiesBundle parametersInfo = queriesModel.addQuery(queryName);
        PropertiesBundle tableInfo = queriesModel.getTable(queryName);

        tableInfo.addProperty("FIRST_NAME", "Фамилия", PropertyType.STRING);
        tableInfo.addProperty("SECOND_NAME", "Имя", PropertyType.STRING);
        tableInfo.addProperty("MIDDLE_NAME", "Отчество", PropertyType.STRING);
        tableInfo.addProperty("DISCOUNT", "Скидка", PropertyType.INTEGER);
        tableInfo.addProperty("MONEY_SPENT", "Сумма заказов", PropertyType.DOUBLE);

        parametersInfo.addReferenceProperty("outlet_id", "Торговая точка", "Outlets");
        parametersInfo.addProperty("min_discount", "Мин. скидка", PropertyType.INTEGER);
        parametersInfo.addProperty("min_orders_total", "Мин. сумма заказов", PropertyType.INTEGER);

        queriesModel.grantAccess(Role.ADMINISTRATOR.toString(), queryName);
        queriesModel.grantAccess(Role.ACCOUNTANT.toString(), queryName);
    }

    private void createQuery9(){
        String queryName = "9. Сумма выручки от реализации фототоваров";

        PropertiesBundle parametersInfo = queriesModel.addQuery(queryName);
        PropertiesBundle tableInfo = queriesModel.getTable(queryName);

        tableInfo.addProperty("TOTAL", "Сумма", PropertyType.DOUBLE);

        parametersInfo.addProperty("start_date", "Дата заказа (с)", PropertyType.DATE);
        parametersInfo.addProperty("end_date", "Дата заказа (по)", PropertyType.DATE);
        parametersInfo.addReferenceProperty("related_outlet_id", "Филиал", "Outlets");

        queriesModel.grantAccess(Role.ADMINISTRATOR.toString(), queryName);
        queriesModel.grantAccess(Role.ACCOUNTANT.toString(), queryName);
    }

    private void createQuery10(){
        String queryName = "10. Перечень фототоваров с наибольшим спросом";

        PropertiesBundle parametersInfo = queriesModel.addQuery(queryName);
        PropertiesBundle tableInfo = queriesModel.getTable(queryName);

//        tableInfo.addReferenceProperty("ID", "ID товара", PropertyType.REFERENCE);
        tableInfo.addProperty("PRODUCT_NAME", "Название товара", PropertyType.STRING);
        tableInfo.addProperty("SALES_COUNT", "Количество продаж", PropertyType.INTEGER);

        parametersInfo.addReferenceProperty("related_outlet_id", "Филиал", "Outlets");

        queriesModel.grantAccess(Role.ADMINISTRATOR.toString(), queryName);
        queriesModel.grantAccess(Role.BUYER.toString(), queryName);
    }

    private void createQuery11(){
        String queryName = "11. Объемы реализации фототоваров";

        PropertiesBundle parametersInfo = queriesModel.addQuery(queryName);
        PropertiesBundle tableInfo = queriesModel.getTable(queryName);

//        tableInfo.addReferenceProperty("ID", "ID товара", PropertyType.REFERENCE);
        tableInfo.addProperty("PRODUCT_NAME", "Название товара", PropertyType.STRING);
        tableInfo.addProperty("SALES_COUNT", "Количество продаж", PropertyType.INTEGER);

        parametersInfo.addProperty("start_date", "Дата заказа (с)", PropertyType.DATE);
        parametersInfo.addProperty("end_date", "Дата заказа (по)", PropertyType.DATE);
        parametersInfo.addReferenceProperty("related_outlet_id", "Филиал", "Outlets");

        queriesModel.grantAccess(Role.ADMINISTRATOR.toString(), queryName);
        queriesModel.grantAccess(Role.BUYER.toString(), queryName);
    }

    private void createQuery12(){
        String queryName = "12. Перечень рабочих мест";

        PropertiesBundle parametersInfo = queriesModel.addQuery(queryName);
        PropertiesBundle tableInfo = queriesModel.getTable(queryName);

        tableInfo.addReferenceProperty("PROFESSION_ID", "Профессия", "Professions");
        tableInfo.addReferenceProperty("OUTLET_ID", "Торговая точка", "Outlets");
        tableInfo.addProperty("AMOUNT", "Количество", PropertyType.INTEGER);

        parametersInfo.addReferenceProperty("profession_id", "Профессия", "Professions");


        queriesModel.grantAccess(Role.ADMINISTRATOR.toString(), queryName);
        queriesModel.grantAccess(Role.HR_MANAGER.toString(), queryName);
    }

    public QueriesModel getModel(){
        return queriesModel;
    }
}
