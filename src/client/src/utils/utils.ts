
export const getToken =():string=>{
    return localStorage.getItem('token') as string;
}

export const getHost = ():string=>{
    const { protocol, host } = window.location;
    const url=`${protocol}//${host}`
    if(url==="http://localhost:3000"){
        return "http://localhost:5001";
    }
    return url;
}