package warehouse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class ProductList implements Collection<StockableProduct> {

	private ArrayList<StockableProduct> list = new ArrayList<StockableProduct>();
	private float totalCost, totalPrice, totalBenefit;
	
	public ProductList(ArrayList<StockableProduct> list) {
		this.list.addAll(list);
		updateCosts();
	}
	public ProductList(StockableProduct... products) {
		for (int i=0, n=products.length; i<n; i++) {
			this.list.add(products[i]);
		}
		updateCosts();
	}

	private void updateCosts() {
		totalCost = 0;
		totalPrice = 0;
		totalBenefit = 0;
		for (StockableProduct product : list) {
			totalCost += product.getTotalCost();
			totalPrice += product.getTotalPrice();
			totalBenefit += totalPrice-totalCost;
		}
	}
	public double calculateCost() {
		double cost = 0;
		for (StockableProduct product : list) {
			cost += product.getTotalCost();
		}
		return cost;
	}
	public double calculatePrice() {
		double price = 0;
		for (StockableProduct product : list) {
			price += product.getTotalPrice();
		}
		return price;
	}
	public double calculateBenefit() {
		return calculatePrice() - calculateCost();
	}
	public StockableProduct mostExpensiveProduct() {
		StockableProduct mostExpensive = null;
		for (StockableProduct product : list) {
			if (mostExpensive == null || product.getTotalPrice() > mostExpensive.getTotalPrice()) {
				mostExpensive = product;
			}
		}
		return mostExpensive;
	}
	public StockableProduct cheapestProduct() {
		StockableProduct cheapest = null;
		for (StockableProduct product : list) {
			if (cheapest == null || product.getTotalPrice() < cheapest.getTotalPrice()) {
				cheapest = product;
			}
		}
		return cheapest;
	}


	// Getters and Setters
	// TODO: Add get(var) and set(data) methods
	public ArrayList<StockableProduct> getList() {
		return list;
	}
	public float getTotalCost() {
		return totalCost;
	}
	public float getTotalPrice() {
		return totalPrice;
	}
	public float getTotalBenefit() {
		return totalBenefit;
	}

	// Collection methods
	@Override
	public int size() {
		return list.size();
	}
	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}
	@Override
	public boolean contains(Object o) {
		return list.contains(o);
	}
	@Override
	public Iterator<StockableProduct> iterator() {
		return list.iterator();
	}
	@Override
	public Object[] toArray() {
		return list.toArray();
	}
	@Override
	public <T> T[] toArray(T[] a) {
		return list.toArray(a);
	}
	@Override
	public boolean add(StockableProduct e) {
		return list.add(e);
	}
	@Override
	public boolean remove(Object o) {
		return list.remove(o);
	}
	@Override
	public boolean containsAll(Collection<?> c) {
		return list.containsAll(c);
	}
	@Override
	public boolean addAll(Collection<? extends StockableProduct> c) {
		return list.addAll(c);
	}
	@Override
	public boolean removeAll(Collection<?> c) {
		return list.removeAll(c);
	}
	@Override
	public boolean retainAll(Collection<?> c) {
		return list.retainAll(c);
	}
	@Override
	public void clear() {
		list.clear();
	}
	
	// TODO: Override toString() method
	// TODO: Add print() and writeToFile(String file) methods
	// TODO: Add static readFromStdio() and readFromFile(String file) methods
}
