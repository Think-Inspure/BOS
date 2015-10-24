package cn.itcast.crm.service.impl;

import java.util.List;

import org.hibernate.Session;

import cn.itcast.crm.domain.Customer;
import cn.itcast.crm.service.CustomerService;
import cn.itcast.crm.utils.HibernateUtils;

public class CustomerServiceImpl implements CustomerService {

	// 查询没有关联到定区的客户
	public List<Customer> findnoassociationCustomers() {
		Session session = HibernateUtils.openSession();
		session.beginTransaction();

		String hql = "from Customer where decidedzone_id is null";
		List<Customer> customers = session.createQuery(hql).list();

		session.getTransaction().commit();
		session.close();

		return customers;
	}

	/**
	 * 查询已经关联到指定定区的客户
	 */
	public List<Customer> findhasassociationCustomers(String decidedZoneId) {
		Session session = HibernateUtils.openSession();
		session.beginTransaction();

		String hql = "from Customer where decidedzone_id = ?";
		List<Customer> customers = session.createQuery(hql)
				.setParameter(0, decidedZoneId).list();

		session.getTransaction().commit();
		session.close();

		return customers;
	}

	/**
	 * 将客户关联到定区
	 */
	public void assignCustomersToDecidedZone(Integer[] customerIds,
			String decidedZoneId) {
		Session session = HibernateUtils.openSession();
		session.beginTransaction();

		// 取消定区所有关联客户
		String hql2 = "update Customer set decidedzone_id=null where decidedzone_id=?";
		session.createQuery(hql2).setParameter(0, decidedZoneId)
				.executeUpdate();

		// 进行关联
		String hql = "update Customer set decidedzone_id=? where id =?";
		if (customerIds != null) {
			for (Integer id : customerIds) {
				session.createQuery(hql).setParameter(0, decidedZoneId)
						.setParameter(1, id).executeUpdate();
			}
		}
		session.getTransaction().commit();
		session.close();
	}

	// 根据用户的电话号码查询用户信息
	public Customer findCustomerByPhoneNumber(String phonenumber) {
		Session session = HibernateUtils.openSession();
		session.beginTransaction();

		String hql = "from Customer where telephone = ?";
		List<Customer> customers = session.createQuery(hql)
				.setParameter(0, phonenumber).list();

		Customer c = null;
		if (customers != null && customers.size() > 0) {
			c = customers.get(0);
		}

		session.getTransaction().commit();
		session.close();

		return c;
	}
	// 根据取件地址查询定区ID
	public String findDecidedzoneIdByAddress(String address) {
		Session session = HibernateUtils.openSession();
		session.beginTransaction();

		String hql = "select decidedzone_id from Customer where address = ?";
		List<String> list = session.createQuery(hql)
				.setParameter(0, address).list();
		String addr = null;
		if (list != null && list.size() > 0) {
			addr = list.get(0);
		}
		session.getTransaction().commit();
		session.close();

		return addr;
	}

}
