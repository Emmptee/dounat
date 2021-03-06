package com.donut.app.http.message;

import java.util.List;

public class JobCodeResponse extends BaseResponse
{

    private List<JobCode> jobList;

    public JobCodeResponse()
    {

    }

    /**
     * @return jobList
     */
    public List<JobCode> getJobList()
    {
        return jobList;
    }

    /**
     * @param jobList
     *            the jobList to set
     */
    public void setJobList(List<JobCode> jobList)
    {
        this.jobList = jobList;
    }

    public class JobCode {
        private String code;

        private String name;

        private Integer type;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code == null ? null : code.trim();
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name == null ? null : name.trim();
        }

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }
    }
}
