import NotLoggedIn from '../../components/NotLoggedIn.jsx'

export default function Chat() {
    const token = localStorage.getItem('token')
    if (!token) {
        return <NotLoggedIn />
    }
    return (
        <div>
            <h1>Chat</h1>
        </div>
    )
}