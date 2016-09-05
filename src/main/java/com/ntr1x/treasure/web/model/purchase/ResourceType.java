package com.ntr1x.treasure.web.model.purchase;

public enum ResourceType{
    
	ROOT_PURCHASE,					// тип базового ресурса поставки, на базе которого создается закупка пользователя
	ROOT_GOOD,						// тип базового ресурса товара, на базе которого создается товар пользователя
	ROOT_COMPANY,
	ROOT_CART,
	ROOT_USER,
	ROOT_SELLER,
	ROOT_MODERATOR,
	ROOT_DELIVERY,
	ROOT_ORDER,
	ROOT_PRIME,
	ROOT_PAYMENT_METHOD,			// тип базового ресурса платежного метода, на базе которого создается платежный метод пользователя
	ROOT_DELIVERY_PLACE,			// тип базового ресурса Центра выдачи, на базе которого создается ЦВ
	EXTENDED						// тип ресурса по умрочанию, выставляемый отнаследованным ресурсам от базовых
}
