create table if not exists group_info
(
    id varchar(32) not null primary key comment '主键',
    name varchar(255) null  unique comment '名称',
    code varchar(255) null  unique comment '代码',
    editable boolean null comment '是否允许编辑',
    `only` boolean null comment '是否排斥其他分组',
    remark varchar(255) null comment '备注'
) comment '分组';

create table if not exists workflow
(
    id varchar(32) not null primary key comment '主键',
    name varchar(255) null  unique comment '流程名称',
    code varchar(255) null  unique comment '流程代码',
    recorder_creator_class_name varchar(255) null comment '实例对应表单数据生成器的类名',
    created timestamp null comment '创建时间',
    configs varchar(255) null
) comment '流程定义';

create table if not exists attachment
(
    id varchar(32) not null primary key comment '主键',
    name varchar(255) null comment '附件名称',
    `type` varchar(100) null comment '附件类别',
    content longblob null comment '附件内容',
    `size` int null comment '附件大小',
    content_type varchar(255) null comment 'contentType',
    recorder_type varchar(255) null comment '记录类型',
    recorder_id varchar(255) null comment '记录id',
    create_user_id varchar(255) null comment '创建人id',
    create_username varchar(255) null comment '创建人帐号',
    create_user_full_name varchar(255) null comment '创建人姓名',
    create_time timestamp null comment '创建时间'
) comment '附件';

create table if not exists dictionary_code
(
    id varchar(32) not null primary key comment '主键',
    code varchar(255) null  unique comment '代码',
    name varchar(255) null comment '名称',
    parent_id varchar(32) null comment '父级id',
    `index` int null comment '排序',
    remark varchar(255) null comment '备注',
    editable boolean null comment '是否允许编辑',
    f1 varchar(255) null comment '备用字段1',
    f2 varchar(255) null comment '备用字段2',
    f3 varchar(255) null comment '备用字段3',
    f4 varchar(255) null comment '备用字段4',
    f5 varchar(255) null comment '备用字段5',
    constraint fk_dictionary_code_parent_id foreign key (parent_id) references dictionary_code (id)
) comment '数据字典';

create table if not exists dynamic_system_configs
(
    config_type varchar(100) not null primary key comment '配置类型',
    config varchar(255) null comment '配置：json 字符串'
) comment '系统动态配置';

create table if not exists brand
(
    id varchar(32) not null primary key comment '主键',
    name varchar(255) null comment '品牌名称',
    order_number int null comment '排序',
    remark varchar(255) null comment '备注'
) comment '品牌';

create table if not exists login_attempt
(
    id varchar(32) not null primary key comment '主键',
    user_id varchar(255) null comment '用户id',
    username varchar(255) null comment '用户名',
    user_full_name varchar(255) null comment '用户姓名',
    ip varchar(255) null comment '登录的IP',
    create_time timestamp null comment '登录时间',
    success boolean null comment '是否成功',
    locked boolean null comment '该记录是否已经锁定过'
) comment '用户登录尝试';
create index ix_login_attempt_user_id on login_attempt (user_id);
create index ix_login_attempt_username on login_attempt (username);
create index ix_login_attempt_create_time on login_attempt (create_time);

create table if not exists customer
(
    id varchar(32) not null primary key comment '主键',
    name varchar(255) null comment '名称',
    charge_user_full_name varchar(255) null comment '负责人名称',
    charge_user_email varchar(255) null comment '负责人邮箱',
    charge_user_phone varchar(255) null comment '负责人电话'
) comment '客户';

create table if not exists computer_room
(
    id varchar(32) not null primary key comment '主键',
    name varchar(255) null comment '名称',
    address varchar(255) null comment '地址',
    contacts_name varchar(255) null comment '联系人名称',
    contacts_phone int null comment '联系人电话',
    customer_id varchar(32) null comment '客户编号',
    order_number int null comment '排序',
    constraint fk_computer_room_customer_id foreign key (customer_id) references customer (id)
) comment '机房';

create table if not exists user_info
(
    id varchar(32) not null primary key comment '主键',
    username varchar(255) null comment '用户名，就是账号',
    full_name varchar(255) null comment '用户姓名',
    phone varchar(255) null comment '电话',
    email varchar(255) null comment '邮件',
    rank_id varchar(32) null comment '职级id',
    entry_date timestamp null comment '入职时间',
    `password` varchar(255) null comment '密码',
    `enable` boolean null comment '帐号是否启用',
    locked boolean null comment '帐号被锁定',
    need_change_password_when_login boolean null comment '登录的时候需要修改密码',
    last_time_change_password timestamp null comment '上次修改密码的时间',
    expired_time timestamp null comment '过期时间',
    created timestamp null comment '帐号创建时间，不允许更新',
    `index` varchar(255) null comment '排序',
    constraint fk_user_info_rank_id foreign key (rank_id) references dictionary_code (id)
) comment '用户信息';

create table if not exists authority
(
    group_id varchar(32) not null primary key comment '组Id',
    authority varchar(255) null comment '权限',
    constraint fk_authority_group_id foreign key (group_id) references dictionary_code (id)
) comment '权限和组映射';

create table if not exists workflow_instance
(
    id varchar(32) not null primary key comment '主键',
    workflow_id varchar(32) null comment '关联的流程Id',
    workflow_config varchar(255) null comment '流程配置信息',
    name varchar(255) null comment '实例名称',
    start_time timestamp null comment '实例启动时间',
    finish_time timestamp null comment '实例完成时间',
    start_user_id varchar(255) null,
    start_username varchar(255) null comment '流程启动人帐号',
    start_user_full_name varchar(255) null comment '流程启动人姓名',
    stop_user_id varchar(32) null comment '流程终止用户id',
    stop_username varchar(255) null comment '流程终止人帐号',
    stop_user_full_name varchar(255) null comment '流程终止人姓名',
    `status` varchar(100) null comment '状态',
    current_step_id varchar(32) null comment '当前步骤编号',
    context_text longtext null comment '流程传递的数据',
    remark varchar(255) null comment '备注',
    recorder_id varchar(32) null comment '表单记录id',
    constraint fk_workflow_instance_workflow_id foreign key (workflow_id) references dictionary_code (id) ,
    constraint fk_workflow_instance_stop_user_id foreign key (stop_user_id) references dictionary_code (id) ,
    constraint fk_workflow_instance_current_step_id foreign key (current_step_id) references dictionary_code (id) ,
    constraint fk_workflow_instance_recorder_id foreign key (recorder_id) references dictionary_code (id)
) comment '流程实例';

create table if not exists brand_model
(
    id varchar(32) not null primary key comment '主键',
    name varchar(255) null comment '型号名称',
    brand_id varchar(32) null comment '品牌编号',
    type_id varchar(32) null comment '类型编号',
    order_number int null comment '排序',
    constraint fk_brand_model_brand_id foreign key (brand_id) references brand (id) ,
    constraint fk_brand_model_type_id foreign key (type_id) references dictionary_code (id)
) comment '型号';

create table if not exists audit_log
(
    id varchar(32) not null primary key comment '主键',
    user_id varchar(32) null comment '操作人员id',
    username varchar(255) null comment '操作人账号',
    full_name varchar(255) null comment '操作人姓名',
    ip varchar(255) null comment '访问的IP',
    url varchar(255) null comment '访问地址',
    `method` varchar(255) null comment '请求的类别: GET, POST, DELETE 等',
    request_type varchar(255) null comment '设置请求类型（json|普通请求）',
    params longtext null comment '访问参数，JSON字符串',
    success boolean null comment '是否访问成功',
    details longtext null comment '日志细节',
    module_name varchar(255) null comment '模块名称',
    method_name varchar(255) null comment '方法名称',
    `operation` varchar(255) null comment '操作类型',
    created timestamp null comment '记录时间',
    constraint fk_audit_log_user_id foreign key (user_id) references user_info (id)
) comment '审计日志';

create table if not exists workflow_step_instance
(
    id varchar(32) not null primary key comment '主键',
    code varchar(255) null comment '步骤代码',
    name varchar(255) null comment '步骤名称',
    workflow_instance_id varchar(32) null comment '关联的实例id',
    start_time timestamp null comment '步骤开始时间',
    finish_time timestamp null comment '步骤结束时间',
    description varchar(255) null comment '描述',
    prev_step_instance_id varchar(32) null comment '上一个步骤实例的id',
    establish_branch_name varchar(255) null comment '步骤结束的时候，执行的是哪个分支',
    todo_executors varchar(255) null comment '该步骤的待办人员id列表，是一个json数组',
    `result` varchar(100) null comment '步骤执行结果',
    constraint fk_workflow_step_instance_workflow_instance_id foreign key (workflow_instance_id) references workflow_instance (id) ,
    constraint fk_workflow_step_instance_prev_step_instance_id foreign key (prev_step_instance_id) references dictionary_code (id)
) comment '流程的步骤实例';

create table if not exists post_member
(
    user_id varchar(32) not null comment '用户id',
    post_id varchar(32) not null comment '岗位id',
    constraint fk_post_member_user_id foreign key (user_id) references user_info (id) ,
    constraint fk_post_member_post_id foreign key (post_id) references dictionary_code (id) ,
    constraint pk_post_member primary key (user_id,post_id)
) comment '岗位人员映射';

create table if not exists equipment
(
    id varchar(32) not null primary key comment '主键',
    name varchar(255) null comment '名称',
    brand_model_id varchar(32) null comment '型号编号',
    computer_room_id varchar(32) null comment '机房编号',
    serial_number varchar(255) null comment '序列号',
    manufacture_date timestamp null comment '生产日期',
    create_time timestamp null comment '创建时间',
    delivery_date timestamp null comment '收货日期',
    install_date timestamp null comment '安装日期',
    start_date timestamp null comment '启用日期',
    `state` boolean null comment '是否故障',
    constraint fk_equipment_brand_model_id foreign key (brand_model_id) references brand_model (id) ,
    constraint fk_equipment_computer_room_id foreign key (computer_room_id) references computer_room (id)
) comment '设备';

create table if not exists group_member
(
    user_id varchar(32) not null comment '人员id',
    group_id varchar(32) not null comment '分组id',
    constraint fk_group_member_user_id foreign key (user_id) references user_info (id) ,
    constraint fk_group_member_group_id foreign key (group_id) references group_info (id) ,
    constraint pk_group_member primary key (user_id,group_id)
) comment '分组人员映射';

create table if not exists department
(
    id varchar(32) not null primary key comment '主键',
    name varchar(255) null comment '部门名称',
    parent_id varchar(32) null comment '父级部门id',
    charge_user_id varchar(32) null comment '部门分管领导id',
    order_number int null comment '排序',
    constraint fk_department_parent_id foreign key (parent_id) references department (id) ,
    constraint fk_department_charge_user_id foreign key (charge_user_id) references user_info (id)
) comment '部门';

create table if not exists department_member
(
    user_id varchar(32) not null comment '用户id',
    department_id varchar(32) not null comment '部门id',
    main_department boolean null comment '是否是该用户的主部门',
    constraint fk_department_member_user_id foreign key (user_id) references user_info (id) ,
    constraint fk_department_member_department_id foreign key (department_id) references department (id) ,
    constraint pk_department_member primary key (user_id,department_id)
) comment '部门人员映射';

create table if not exists workflow_task_info
(
    id varchar(32) not null primary key comment '主键',
    step_instance_id varchar(32) null comment '步骤实例id',
    start_date_time timestamp null comment '任务启动时间',
    finish_date_time timestamp null comment '任务完成时间',
    deadline timestamp null comment '任务超时时间',
    executor_id varchar(32) null comment '任务分配给执行对象的id，这里可能是人员ID，部门ID，岗位ID，分组ID',
    executor_name varchar(255) null comment '任务分配给执行对象的名称',
    actual_executor_id varchar(32) null comment '实际任务执行者id，这里一定是一个人员id',
    actual_executor_name varchar(255) null comment '实际任务执行者姓名',
    `action` varchar(100) null comment '执行动作',
    `status` varchar(100) null comment '任务状态',
    remark varchar(255) null comment '处理意见',
    assign_from_user_id varchar(32) null comment '任务指派用户id',
    assign_from_user_name varchar(255) null comment '任务指派用户姓名',
    description varchar(255) null comment '任务描述',
    constraint fk_workflow_task_info_step_instance_id foreign key (step_instance_id) references workflow_step_instance (id) ,
    constraint fk_workflow_task_info_executor_id foreign key (executor_id) references dictionary_code (id) ,
    constraint fk_workflow_task_info_actual_executor_id foreign key (actual_executor_id) references dictionary_code (id) ,
    constraint fk_workflow_task_info_assign_from_user_id foreign key (assign_from_user_id) references dictionary_code (id)
) comment '流程任务';

-- 三员;
insert into group_info(id, code, name, editable, only)
values ('158e534a75cc4b0fa63b955e9cbcca8a', 'gly', '系统管理员', false, true),
       ('8faa0f644d474c489d79f35fac098d38', 'sjy', '安全审计员', false, true),
       ('1839ef8afc464b82b66568642e74756f', 'aqy', '安全保密管理员', false, true);

-- 默认分组;
insert into group_info(id, code, name, editable)
values ('c64a54e6c5db436990409579f8b2b6ee', 'WorkflowAdmin', '流程管理员', false);

-- 加一个默认用户，密码是1234@abcd;
insert into user_info(id, username, password, full_name, enable, locked, need_change_password_when_login, created)
values ('e4d332f46be642b5a0aa8c07191e4fdf', 'user1', '$2a$10$Tae72Uw/ahmqYWlbxQy/Y.wg2ypr4YGJwbezZ1hMcv1MHvPKPMEju', '第一个用户', true, false, false,
        current_timestamp());

insert into group_member(user_id, group_id) values ('e4d332f46be642b5a0aa8c07191e4fdf', '158e534a75cc4b0fa63b955e9cbcca8a');
