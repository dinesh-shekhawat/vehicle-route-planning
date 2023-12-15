package edu.northeastern.csye6220.vehicleRoutePlanning.repository.impl;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.northeastern.csye6220.vehicleRoutePlanning.entities.Otp;
import edu.northeastern.csye6220.vehicleRoutePlanning.hibernate.HibernateConnectionService;
import edu.northeastern.csye6220.vehicleRoutePlanning.repository.OtpRepository;

@Service
public class OtpRepositoryImpl extends AbstractEntityRepositoryImpl<Otp> implements OtpRepository {
	private static final Logger LOGGER = LoggerFactory.getLogger(OtpRepositoryImpl.class);

	@Autowired
	public OtpRepositoryImpl(HibernateConnectionService hibernateConnectionService) {
		register(hibernateConnectionService.getSessionFactory(), Otp.class);
	}

	@Override
	public Otp findLatestByEmailIdAndField(String emailId, String field) {
		LOGGER.trace("findLatestByEmailIdAndField for emailId: {}, field: {}", emailId, field);
		
		Otp otp = null;
		
		try (Session session = sessionFactory.openSession()) {
			String hql = "FROM Otp WHERE issuingEmailId = :emailId AND field = :field AND accessedOn = NULL AND deleted = :deleted";
			Query<Otp> query = session.createQuery(hql, Otp.class);
			query.setParameter("emailId", emailId);
			query.setParameter("field", field);
			query.setParameter("deleted", false);

			otp = query.uniqueResult();
		} catch (Exception e) {
			LOGGER.error("Exception in findByEmailIdAndNotDeleted: {}", e.getMessage(), e);
			throw e;
		}
		
		LOGGER.trace("found the otp: {}", otp);
		return otp;
	}
	
}
