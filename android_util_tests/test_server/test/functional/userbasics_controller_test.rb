require 'test_helper'

class UserbasicsControllerTest < ActionController::TestCase
  def test_should_get_index
    get :index
    assert_response :success
    assert_not_nil assigns(:userbasics)
  end

  def test_should_get_new
    get :new
    assert_response :success
  end

  def test_should_create_userbasic
    assert_difference('Userbasic.count') do
      post :create, :userbasic => { }
    end

    assert_redirected_to userbasic_path(assigns(:userbasic))
  end

  def test_should_show_userbasic
    get :show, :id => userbasics(:one).id
    assert_response :success
  end

  def test_should_get_edit
    get :edit, :id => userbasics(:one).id
    assert_response :success
  end

  def test_should_update_userbasic
    put :update, :id => userbasics(:one).id, :userbasic => { }
    assert_redirected_to userbasic_path(assigns(:userbasic))
  end

  def test_should_destroy_userbasic
    assert_difference('Userbasic.count', -1) do
      delete :destroy, :id => userbasics(:one).id
    end

    assert_redirected_to userbasics_path
  end
end
