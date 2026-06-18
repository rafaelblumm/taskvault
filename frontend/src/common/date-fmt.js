export const formatDate = (dateString) => {
    return dateString ? new Date(dateString).toLocaleDateString('pt-BR') : '-'
}

export const formatDateTime = (dateTimeString) => {
    return dateTimeString ? new Date(dateTimeString).toLocaleString('pt-BR') : '-'
}
