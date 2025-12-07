import {NextRequest, NextResponse} from "next/server";

async function proxyRequest(request : NextRequest){
    // lay path tu Fe
    const pathFe = request.nextUrl.pathname.replace('api/proxy','');
    const method = request.method ;

    const init : RequestInit = {method};
    if(method != 'GET'){
        init.body = await request.text();
        init.headers = { 'Content-Type': 'application/json' };
    }

    const res = await fetch(`http://localhost:8080${pathFe }`, init);
    const data = await res.text();

    // Trả về FE
    return new NextResponse(data, { status: res.status });
}

export { proxyRequest as GET, proxyRequest as POST, proxyRequest as PUT, proxyRequest as DELETE };