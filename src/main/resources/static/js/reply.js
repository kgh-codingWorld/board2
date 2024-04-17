async function get1(bno) {

    const result = await axios.get(`/replies/list/${bno}`)

    return result; //결과 반환용으로 구현 완료 read.html에 then(), catch()등을 이용함.
}

async function getList({bno, page, size, goLast}){  // 577 추가

    const result = await axios.get(`/replies/list/${bno}`, {params: {page, size}})

    // 582 추가 (댓글 마지막 페이지로 호출)
    if(goLast){
        const total = result.data.total
        const lastPage = parseInt(Math.ceil(total/size))

        return getList({bno:bno, page:lastPage, size:size})

    }
    return result.data
}



async function addReply(replyObj) { // 584 추가
    const response = await axios.post(`/replies/`,replyObj)
    return response.data
}

async function getReply(rno) { // 588 추가
    const response = await axios.get(`/replies/${rno}`)
    return response.data
}

async function modifyReply(replyObj) { // 589 추가

    const response = await axios.put(`/replies/${replyObj.rno}`, replyObj)
    return response.data
}

async function removeReply(rno) { // 593 추가
    const response = await axios.delete(`/replies/${rno}`)
    return response.data
}