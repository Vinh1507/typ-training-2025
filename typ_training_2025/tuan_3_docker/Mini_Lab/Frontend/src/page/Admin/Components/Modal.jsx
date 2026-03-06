export default function Modal() {
    return (
        <div>
            <h1>Modal</h1>
        </div>
    )
}

export default function Button() {
    return <button>Click me</button>
}

export default function Input() {
    return <input type="text" placeholder="Enter your name" />
}

export default function Select() {
    return <select>
        <option value="1">Option 1</option>
        <option value="2">Option 2</option>
        <option value="3">Option 3</option>
    </select>
}

export default function Textarea() {
    return <textarea placeholder="Enter your message" />
}

export default function Radio() {
    return <input type="radio" name="option" value="1" />
}

export default function Checkbox() {
    return <input type="checkbox" name="option" value="1" />
}
