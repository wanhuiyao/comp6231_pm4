package tools.message;

public enum Action {
	ACK, INIT, REPLICA_RESULT, HEART_BEAT, REPLICA_CRUSH,
	sync, askSync, doSync, askInitSync,
	signUp, signIn, getCatelog, submitOrder,
	shippingGoods, getProducts, getProductsByRegisteredManufacturers, getProductsByID, getProductsByType,
	processPurchaseOrder, getProductInfo, receivePayment, getProductList,
	test
}
