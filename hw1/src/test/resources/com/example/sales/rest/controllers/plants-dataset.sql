insert into plant_inventory_entry (id, name, description, price) values (1, 'Mini excavator', '1.5 Tonne Mini excavator', 150);
insert into plant_inventory_entry (id, name, description, price) values (2, 'Mini excavator', '3 Tonne Mini excavator', 200);
insert into plant_inventory_entry (id, name, description, price) values (3, 'Midi excavator', '5 Tonne Midi excavator', 250);
insert into plant_inventory_entry (id, name, description, price) values (4, 'Midi excavator', '8 Tonne Midi excavator', 300);
insert into plant_inventory_entry (id, name, description, price) values (5, 'Maxi excavator', '15 Tonne Large excavator', 400);
insert into plant_inventory_entry (id, name, description, price) values (6, 'Maxi excavator', '20 Tonne Large excavator', 450);
insert into plant_inventory_entry (id, name, description, price) values (7, 'HS dumper', '1.5 Tonne Hi-Swivel Dumper', 150);
insert into plant_inventory_entry (id, name, description, price) values (8, 'FT dumper', '2 Tonne Front Tip Dumper', 180);
insert into plant_inventory_entry (id, name, description, price) values (9, 'FT dumper', '2 Tonne Front Tip Dumper', 200);
insert into plant_inventory_entry (id, name, description, price) values (10, 'FT dumper', '2 Tonne Front Tip Dumper', 300);
insert into plant_inventory_entry (id, name, description, price) values (11, 'FT dumper', '3 Tonne Front Tip Dumper', 400);
insert into plant_inventory_entry (id, name, description, price) values (12, 'Loader', 'Hewden Backhoe Loader', 200);
insert into plant_inventory_entry (id, name, description, price) values (13, 'D-Truck', '15 Tonne Articulating Dump Truck', 250);
insert into plant_inventory_entry (id, name, description, price) values (14, 'D-Truck', '30 Tonne Articulating Dump Truck', 300);

insert into plant_inventory_item (id, plant_info_id, serial_number, equipment_condition) values (1, 1, 'A01', 'SERVICEABLE');
insert into plant_inventory_item (id, plant_info_id, serial_number, equipment_condition) values (2, 1, 'A02', 'SERVICEABLE');
insert into plant_inventory_item (id, plant_info_id, serial_number, equipment_condition) values (15, 2, 'A017', 'SERVICEABLE');
insert into plant_inventory_item (id, plant_info_id, serial_number, equipment_condition) values (3, 3, 'A03', 'UNSERVICEABLE_REPAIRABLE');
insert into plant_inventory_item (id, plant_info_id, serial_number, equipment_condition) values (4, 3, 'A04', 'UNSERVICEABLE_REPAIRABLE');
insert into plant_inventory_item (id, plant_info_id, serial_number, equipment_condition) values (5, 3, 'A05', 'UNSERVICEABLE_REPAIRABLE');
insert into plant_inventory_item (id, plant_info_id, serial_number, equipment_condition) values (6, 3, 'A06', 'SERVICEABLE');
insert into plant_inventory_item (id, plant_info_id, serial_number, equipment_condition) values (7, 3, 'A07', 'SERVICEABLE');
insert into plant_inventory_item (id, plant_info_id, serial_number, equipment_condition) values (8, 7, 'A08', 'SERVICEABLE');
insert into plant_inventory_item (id, plant_info_id, serial_number, equipment_condition) values (9, 11, 'A09', 'SERVICEABLE');
insert into plant_inventory_item (id, plant_info_id, serial_number, equipment_condition) values (10, 11, 'A10', 'SERVICEABLE');
insert into plant_inventory_item (id, plant_info_id, serial_number, equipment_condition) values (11, 4, 'A11', 'SERVICEABLE');
insert into plant_inventory_item (id, plant_info_id, serial_number, equipment_condition) values (12, 5, 'A12', 'UNSERVICEABLE_REPAIRABLE');
insert into plant_inventory_item (id, plant_info_id, serial_number, equipment_condition) values (13, 6, 'A13', 'UNSERVICEABLE_REPAIRABLE');

insert into maintenance_plan (id, year_of_action) values (1, 2017);

insert into plant_reservation (id, plant_id, start_date, end_date) values (2, 2, '2015-03-22', '2016-03-24');
insert into plant_reservation (id, plant_id, start_date, end_date) values (3, 3, '2015-03-22', '2016-03-24');
insert into plant_reservation (id, plant_id, start_date, end_date) values (5, 4, '2009-03-22', '2009-03-24');
insert into plant_reservation (id, plant_id, start_date, end_date) values (6, 6, '2009-03-22', '2009-03-24');
insert into plant_reservation (id, plant_id, start_date, end_date) values (7, 7, '2009-03-22', '2009-03-24');
insert into plant_reservation (id, plant_id, start_date, end_date) values (8, 5, '2009-03-22', '2009-03-24');
insert into plant_reservation (id, plant_id, start_date, end_date) values (9, 15, '2016-03-22', '2018-03-24');