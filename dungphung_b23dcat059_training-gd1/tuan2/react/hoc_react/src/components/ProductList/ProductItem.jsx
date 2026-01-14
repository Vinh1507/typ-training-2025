function ProductItem(props) {
    const {item}=props;
    return (
        <>
            <div className="product__item" >
                <img src={item.image} alt="" style={{ width: "100px" }} />
                <div>{item.name}</div>
                <div>{item.price}</div>
            </div>
        </>
    )
}
export default ProductItem;