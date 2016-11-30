CREATE DATABASE /*!32312 IF NOT EXISTS*/`activiti` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `activiti`;

/*Table structure for table `t_employee` */

DROP TABLE IF EXISTS `t_employee`;

CREATE TABLE `t_employee` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) DEFAULT NULL,
  `password` varchar(32) DEFAULT NULL,
  `email` varchar(64) DEFAULT NULL,
  `role` varchar(32) DEFAULT NULL,
  `managerId` int(64) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

/*Data for the table `t_employee` */

insert  into `t_employee`(`id`,`name`,`password`,`email`,`role`,`managerId`) 
values 
(1,'王中军','123','wangzhongjun@163.com','boss',NULL),
(2,'冯小刚经纪人','123','fengxiaogangManager@163.com','manager',1),
(3,'范冰冰经纪人','123','fanbingbingManager@163.com','manager',1),
(4,'冯小刚','123','fengxiaogang@163.com','user',2),
(5,'范冰冰','123','fanbingbing@163.com','user',3);

/*Table structure for table `t_leave` */

DROP TABLE IF EXISTS `t_leave`;

CREATE TABLE `t_leave` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `days` int(11) DEFAULT NULL,
  `content` varchar(512) DEFAULT NULL,
  `remark` varchar(512) DEFAULT NULL,
  `leaveDate` datetime DEFAULT NULL,
  `state` int(11) DEFAULT NULL,
  `employeeId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
