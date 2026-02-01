import {NextRequest, NextResponse} from "next/server";

async function proxyRequest(request : NextRequest){
    // lay path tu Fe - remove /api/proxy prefix
    const pathname = request.nextUrl.pathname;
    const pathFe = pathname.replace('/api/proxy', ''); 

    const method = request.method ;

    // Prepare headers
    const headers = new Headers(request.headers);
    // Remove Origin and Referer to avoid CORS issues when talking direct to backend services
    headers.delete('origin');
    headers.delete('referer');
    headers.delete('host');
    headers.set('Content-Type', 'application/json');

    const init : RequestInit = {
        method,
        headers,
    };
    
    if(method != 'GET'){
        init.body = await request.text();
    }

    let targetUrl;
    
    // Bypass Gateway for Auth AND Users - go direct to Auth Service on Host Port 18081
    if (pathFe.startsWith('/api/auth') || pathFe.startsWith('/api/users')) {
        targetUrl = `http://localhost:18081${pathFe}`;
        console.log(`[Proxy] Bypass Gateway: Forwarding ${pathname} to ${targetUrl}`);
    } else {
        // Use Gateway for everything else
        const apiUrl = process.env.API_URL || 'http://localhost:8090';
        targetUrl = `${apiUrl}${pathFe}`;
        console.log(`[Proxy] Gateway: Forwarding ${pathname} to ${targetUrl}`);
    }
    
    try {
        const res = await fetch(targetUrl, init);
        const data = await res.text();
        
        // Return response to Frontend
        return new NextResponse(data, { 
            status: res.status,
            headers: {
                'Content-Type': 'application/json'
            }
        });
    } catch (e) {
        console.error("[Proxy] Error:", e);
        return new NextResponse(JSON.stringify({message: "Backend unreachable"}), {status: 502});
    }
}

export { proxyRequest as GET, proxyRequest as POST, proxyRequest as PUT, proxyRequest as DELETE };
