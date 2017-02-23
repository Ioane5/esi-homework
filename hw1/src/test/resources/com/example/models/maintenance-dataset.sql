insert into maintenance_plan (id, year_of_action) values (1, 2017);
insert into maintenance_plan (id, year_of_action) values (2, 2017);
insert into maintenance_plan (id, year_of_action) values (3, 2016);
insert into maintenance_plan (id, year_of_action) values (4, 2015);
insert into maintenance_plan (id, year_of_action) values (5, 2014);
insert into maintenance_plan (id, year_of_action) values (6, 2013);
insert into maintenance_plan (id, year_of_action) values (7, 2012);

insert into maintenance_task (id, plan_id, description, price, type_of_work, start_date, end_date) values (1, 1, 'Mini excavator fix 1', 100, 'CORRECTIVE', '2017-03-22', '2017-03-24');
insert into maintenance_task (id, plan_id, description, price, type_of_work, start_date, end_date) values (2, 1, 'Mini excavator fix 2', 100, 'PREVENTIVE', '2017-03-22', '2017-03-24');
insert into maintenance_task (id, plan_id, description, price, type_of_work, start_date, end_date) values (3, 2, 'Mini excavator 2 fix 3', 100, 'CORRECTIVE', '2017-02-22', '2017-02-24');
insert into maintenance_task (id, plan_id, description, price, type_of_work, start_date, end_date) values (4, 3, 'Mini excavator fix 4', 100, 'PREVENTIVE', '2016-03-22', '2016-03-24');
insert into maintenance_task (id, plan_id, description, price, type_of_work, start_date, end_date) values (5, 3, 'Mini excavator fix 5', 100, 'CORRECTIVE', '2016-08-22', '2016-08-24');
insert into maintenance_task (id, plan_id, description, price, type_of_work, start_date, end_date) values (6, 4, 'Mini excavator fix 6', 200, 'CORRECTIVE', '2015-03-22', '2015-03-24');
insert into maintenance_task (id, plan_id, description, price, type_of_work, start_date, end_date) values (7, 4, 'Mini excavator fix 7', 200, 'CORRECTIVE', '2015-03-22', '2015-03-24');
insert into maintenance_task (id, plan_id, description, price, type_of_work, start_date, end_date) values (8, 4, 'Mini excavator fix 8', 200, 'CORRECTIVE', '2015-03-22', '2015-03-24');
insert into maintenance_task (id, plan_id, description, price, type_of_work, start_date, end_date) values (9, 5, 'Mini excavator fix 9', 200, 'PREVENTIVE', '2014-03-22', '2014-03-24');
insert into maintenance_task (id, plan_id, description, price, type_of_work, start_date, end_date) values (10, 5, 'Mini excavator fix 10', 200, 'CORRECTIVE', '2014-03-22', '2014-03-24');
insert into maintenance_task (id, plan_id, description, price, type_of_work, start_date, end_date) values (11, 6, 'Mini excavator fix 10', 500, 'CORRECTIVE', '2013-03-22', '2013-03-24');
insert into maintenance_task (id, plan_id, description, price, type_of_work, start_date, end_date) values (12, 7, 'Mini excavator fix 10', 300, 'CORRECTIVE', '2012-03-22', '2012-03-24');

