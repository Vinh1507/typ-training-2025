-- điều khiển truy cập

-- grant - cấp quyền

-- vd: tạo user và cấp các quyền cho user này
create user 'user1'@'localhost' identified by 'password123';

grant select on database_name.sinhvien to 'user1'@'localhost';
grant insert, update on database_name.sinhvien to 'user1'@'localhost';
grant delete on database_name.diem to 'user1'@'localhost';
grant all privileges on database_name.* to 'user1'@'localhost';

-- revoke - thu hồi quyền

-- vd: thu hồi các quyền của user
revoke select on database_name.sinhvien from 'user1'@'localhost';
revoke insert, update on database_name.sinhvien from 'user1'@'localhost';
revoke all privileges on database_name.* from 'user1'@'localhost';

-- kiểm tra quyền
show grants;
show grants for 'user1'@'localhost';
select user, host from mysql.user;

flush privileges;

-- drop user 'user1'@'localhost';