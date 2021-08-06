package phs;

import model.support.SupportElementsModel;

public class SupportElementsPHS {
    private final SupportElementsModel elementsModel = new SupportElementsModel();

    public SupportElementsPHS() {
        elementsModel.addSupportElement("related_outlets");
        elementsModel.addSupportElement("orders_types");
        elementsModel.addSupportElement("is_paid_development");
        elementsModel.addSupportElement("get_print_order_total");
        elementsModel.addSupportElement("get_discounted_order_total");
        elementsModel.addSupportElement("get_developer_order_total");
        elementsModel.addSupportElement("clients_outlets");
        elementsModel.addSupportElement("ORDERS_TOTALS");
        elementsModel.addSupportElement("update_order_discount");
        elementsModel.addSupportElement("update_items_count");
        elementsModel.addSupportElement("update_receipt_total");

        elementsModel.addSupportElement("related_outlet_tr");
        elementsModel.addSupportElement("urgent_orders_tr");
        elementsModel.addSupportElement("service_orders_outlet_tr");
        elementsModel.addSupportElement("print_orders_outlet_tr");
//        elementsModel.addSupportElement("print_orders_storage_tr");
//        elementsModel.addSupportElement("print_orders_storage_del_tr");
        elementsModel.addSupportElement("developer_orders_outlet_tr");
//        elementsModel.addSupportElement("developer_orders_storage_tr");
//        elementsModel.addSupportElement("dev_orders_storage_del_tr");
        elementsModel.addSupportElement("sale_orders_outlet_tr");
        elementsModel.addSupportElement("sale_orders_items_tr");
//        elementsModel.addSupportElement("sale_orders_storage_tr");
//        elementsModel.addSupportElement("sale_orders_storage_del_tr");
        elementsModel.addSupportElement("delivery_vendor_tr");
//        elementsModel.addSupportElement("delivery_storage_tr");
//        elementsModel.addSupportElement("delivery_storage_del_tr");

        elementsModel.addSupportElement("Clients_seq");
        elementsModel.addSupportElement("Outlet_types_seq");
        elementsModel.addSupportElement("Outlets_seq");
        elementsModel.addSupportElement("Professions_seq");
        elementsModel.addSupportElement("Jobs_seq");
        elementsModel.addSupportElement("Firms_seq");
        elementsModel.addSupportElement("Items_seq");
        elementsModel.addSupportElement("Storage_seq");
        elementsModel.addSupportElement("Vendors_seq");
        elementsModel.addSupportElement("Deliveries_seq");
        elementsModel.addSupportElement("Receipts_seq");
        elementsModel.addSupportElement("Orders_seq");
        elementsModel.addSupportElement("Service_types_seq");
        elementsModel.addSupportElement("Paper_sizes_seq");
        elementsModel.addSupportElement("Paper_types_seq");
        elementsModel.addSupportElement("Print_prices_seq");
        elementsModel.addSupportElement("Print_discounts_seq");
        elementsModel.addSupportElement("Frames_seq");
        elementsModel.addSupportElement("Developer_prices_seq");

        elementsModel.addSupportElement("Clients_id_tr");
        elementsModel.addSupportElement("Outlet_types_id_tr");
        elementsModel.addSupportElement("Outlets_id_tr");
        elementsModel.addSupportElement("Professions_id_tr");
        elementsModel.addSupportElement("Jobs_id_tr");
        elementsModel.addSupportElement("Firms_id_tr");
        elementsModel.addSupportElement("Items_id_tr");
        elementsModel.addSupportElement("Storage_id_tr");
        elementsModel.addSupportElement("Vendors_id_tr");
        elementsModel.addSupportElement("Deliveries_id_tr");
        elementsModel.addSupportElement("Receipts_id_tr");
        elementsModel.addSupportElement("Orders_id_tr");
        elementsModel.addSupportElement("Service_types_id_tr");
        elementsModel.addSupportElement("Paper_sizes_id_tr");
        elementsModel.addSupportElement("Paper_types_id_tr");
        elementsModel.addSupportElement("Print_prices_id_tr");
        elementsModel.addSupportElement("Print_discounts_id_tr");
        elementsModel.addSupportElement("Frames_id_tr");
        elementsModel.addSupportElement("Developer_prices_id_tr");

    }

    public SupportElementsModel getModel(){
        return elementsModel;
    }
}
