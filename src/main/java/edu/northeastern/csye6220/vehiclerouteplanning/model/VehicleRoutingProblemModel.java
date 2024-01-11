package edu.northeastern.csye6220.vehiclerouteplanning.model;

import java.util.List;

public class VehicleRoutingProblemModel {
	private List<VehicleModel> vehicles;
	private List<ServiceModel> services;
	private List<DeliveryModel> deliveries;
	private List<ShipmentModel> shipments;
	
	public List<VehicleModel> getVehicles() {
		return vehicles;
	}
	public void setVehicles(List<VehicleModel> vehicles) {
		this.vehicles = vehicles;
	}
	public List<ServiceModel> getServices() {
		return services;
	}
	public void setServices(List<ServiceModel> services) {
		this.services = services;
	}
	public List<DeliveryModel> getDeliveries() {
		return deliveries;
	}
	public void setDeliveries(List<DeliveryModel> deliveries) {
		this.deliveries = deliveries;
	}
	public List<ShipmentModel> getShipments() {
		return shipments;
	}
	public void setShipments(List<ShipmentModel> shipments) {
		this.shipments = shipments;
	}

	@Override
	public String toString() {
		return "VehicleRoutingProblemModel [vehicles=" + vehicles + ", services=" + services + ", deliveries="
				+ deliveries + ", shipments=" + shipments + "]";
	}
}
