using System;
namespace Asset_API.Models
{
    public class ResultModel<T>
    {
        public int Status { get; set; }
        public string Description { get; set; }
        public T Data { get; set; }

        public ResultModel(int status, string description, T data)
        {
            Status = status;
            Description = description;
            Data = data;
        }
    }
}
