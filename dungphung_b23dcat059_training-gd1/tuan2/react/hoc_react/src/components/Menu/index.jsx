function Menu() {
    const arrayMenu = [
        "Trang chu",
        "San pham",
        "Tin tuc",
        "Gioi thieu",
        "Lien he"
    ]
    return (
        <>
            {/* <ul>
                <li></li>
            </ul> */}
            <ul>
                {arrayMenu.map((item, index) => {
                    return (
                        <li>{item}</li>
                    )
                })}
            </ul>

            
            <ul>
                {arrayMenu.map((item, index) => 
                        (<li>{item}</li>)
                )}
            </ul>

        </>
    )
}
export default Menu;