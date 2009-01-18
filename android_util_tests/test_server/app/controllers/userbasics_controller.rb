class UserbasicsController < ApplicationController
  before_filter :auth

  # GET /userbasics
  # GET /userbasics.xml
  def index
    @userbasics = Userbasic.find(:all)

    respond_to do |format|
      format.html # index.html.erb
      format.xml  { render :xml => @userbasics }
    end
  end

  # GET /userbasics/1
  # GET /userbasics/1.xml
  def show
    @userbasic = Userbasic.find(params[:id])

    respond_to do |format|
      format.html # show.html.erb
      format.xml  { render :xml => @userbasic }
    end
  end

  # GET /userbasics/new
  # GET /userbasics/new.xml
  def new
    @userbasic = Userbasic.new

    respond_to do |format|
      format.html # new.html.erb
      format.xml  { render :xml => @userbasic }
    end
  end

  # GET /userbasics/1/edit
  def edit
    @userbasic = Userbasic.find(params[:id])
  end

  # POST /userbasics
  # POST /userbasics.xml
  def create
    @userbasic = Userbasic.new(params[:userbasic])

    respond_to do |format|
      if @userbasic.save
        flash[:notice] = 'Userbasic was successfully created.'
        format.html { redirect_to(@userbasic) }
        format.xml  { render :xml => @userbasic, :status => :created, :location => @userbasic }
      else
        format.html { render :action => "new" }
        format.xml  { render :xml => @userbasic.errors, :status => :unprocessable_entity }
      end
    end
  end

  # PUT /userbasics/1
  # PUT /userbasics/1.xml
  def update
    @userbasic = Userbasic.find(params[:id])

    respond_to do |format|
      if @userbasic.update_attributes(params[:userbasic])
        flash[:notice] = 'Userbasic was successfully updated.'
        format.html { redirect_to(@userbasic) }
        format.xml  { head :ok }
      else
        format.html { render :action => "edit" }
        format.xml  { render :xml => @userbasic.errors, :status => :unprocessable_entity }
      end
    end
  end

  # DELETE /userbasics/1
  # DELETE /userbasics/1.xml
  def destroy
    @userbasic = Userbasic.find(params[:id])
    @userbasic.destroy

    respond_to do |format|
      format.html { redirect_to(userbasics_url) }
      format.xml  { head :ok }
    end
  end

private

  def auth
    authenticate_or_request_with_http_basic do |user, pass|
      user == 'basic' && pass == 'pass'
    end
  end


end
