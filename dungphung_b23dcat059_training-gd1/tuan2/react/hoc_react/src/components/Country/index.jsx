import { country } from "../../data/country"
function Country() {
    console.log(country);
    return (
        <>
            <ul>
                {country.map(itemCountry => (
                    <li key={itemCountry.id}>
                        <span>{itemCountry.name}</span>
                        <ul>
                            {(itemCountry || []).city.map(itemCity => (
                                <li key={itemCity.id}>{itemCity.name}</li>
                            ))}
                        </ul>

                    </li>
                ))}
            </ul>

        </>
    )
}
export default Country